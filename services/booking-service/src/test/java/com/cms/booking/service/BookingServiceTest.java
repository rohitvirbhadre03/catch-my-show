package com.cms.booking.service;

import com.cms.booking.dto.BookingCreateRequest;
import com.cms.booking.notify.NotificationPublisher;
import com.cms.booking.repository.BookingRepository;
import com.cms.booking.repository.SeatLockRepository;
import com.cms.shared.model.BookingCreatedMessage;
import com.cms.shared.model.BookingEntity;
import com.cms.shared.model.SeatLockEntity;
import com.cms.shared.types.SeatLockStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingsRepository;
    @Mock
    private SeatLockRepository seatLockRepository;
    @Mock
    private NotificationPublisher notificationPublisher;

    @InjectMocks
    private BookingService bookingService;

    @Captor
    ArgumentCaptor<List<SeatLockEntity>> locksCaptor;

    @BeforeEach
    void initSync() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.initSynchronization();
        }
    }

    @AfterEach
    void clearSync() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void create_success_publishesNotification_afterCommit() {
        long showId = 101L;
        long userId = 42L;
        var seats = List.of("A1", "A2");

        var req = new BookingCreateRequest(
                showId,
                seats,
                1000,
                null,
                0,
                0,
                1000,
                userId,
                300
        );

        when(seatLockRepository.findActiveLocks(eq(showId), eq(seats), eq(SeatLockStatus.HELD.name()), any()))
                .thenReturn(Collections.emptyList());

        when(bookingsRepository.save(any(BookingEntity.class)))
                .thenAnswer(inv -> {
                    var e = inv.getArgument(0, BookingEntity.class);
                    e.setId(999L);
                    e.setCreatedAt(OffsetDateTime.now());
                    return e;
                });

        var response = bookingService.create(req);

        assertThat(response.id()).isEqualTo(999L);

        verify(seatLockRepository).saveAll(locksCaptor.capture());
        List<SeatLockEntity> locks = locksCaptor.getValue();

        assertThat(locks)
                .hasSize(seats.size())
                .allSatisfy(lock -> {
                    assertThat(lock.getShowId()).isEqualTo(showId);
                    assertThat(seats).contains(lock.getSeatLabel());
                    assertThat(lock.getHoldFrom()).isNotNull();
                    assertThat(lock.getHoldUntil()).isNotNull();
                });

        verify(seatLockRepository).expireHolds(eq(SeatLockStatus.HELD.name()), eq(SeatLockStatus.EXPIRED.name()), any(OffsetDateTime.class));

        var syncs = TransactionSynchronizationManager.getSynchronizations();
        assertThat(syncs).isNotEmpty();
        syncs.forEach(TransactionSynchronization::afterCommit);

        ArgumentCaptor<BookingCreatedMessage> msgCap = ArgumentCaptor.forClass(BookingCreatedMessage.class);
        verify(notificationPublisher, times(1)).publishBookingCreated(msgCap.capture());

        var msg = msgCap.getValue();
        assertThat(msg.bookingId()).isEqualTo(999L);
        assertThat(msg.userId()).isEqualTo(userId);
        assertThat(msg.showId()).isEqualTo(showId);
        assertThat(msg.seats()).containsExactlyElementsOf(seats);
        assertThat(msg.total()).isEqualTo(1000);
        assertThat(msg.createdAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void create_whenSeatsAlreadyHeld_throws_andDoesNotPersistOrNotify() {
        long showId = 101L;
        long userId = 42L;
        var seats = List.of("A1", "A2");

        var req = new BookingCreateRequest(
                showId,
                seats,
                1000,
                null,
                0,
                0,
                1000,
                userId,
                300
        );

        when(seatLockRepository.findActiveLocks(eq(showId), eq(seats), eq(SeatLockStatus.HELD.name()), any()))
                .thenReturn(List.of(new SeatLockEntity()));

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(req));

        verify(seatLockRepository, never()).saveAll(anyList());
        verify(bookingsRepository, never()).save(any());
        verify(notificationPublisher, never()).publishBookingCreated(any());
    }
}