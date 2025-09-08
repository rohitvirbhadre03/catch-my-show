package com.cms.booking.service;

import com.cms.shared.model.BookingCreatedMessage;
import com.cms.shared.model.BookingEntity;
import com.cms.shared.model.SeatLockEntity;
import com.cms.shared.types.SeatLockStatus;
import com.cms.booking.dto.BookingCreateRequest;
import com.cms.booking.dto.BookingResponse;
import com.cms.booking.notify.NotificationPublisher;
import com.cms.booking.repository.BookingRepository;
import com.cms.booking.repository.SeatLockRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingsRepository;
    private final SeatLockRepository seatLockRepository;
    private final NotificationPublisher notificationPublisher;

    public List<BookingResponse> get() {
        return bookingsRepository.findAll()
                .stream()
                .map(BookingResponse::fromEntity)
                .toList();
    }

    @Transactional
    public BookingResponse create(BookingCreateRequest request) {

        System.out.println("Creating booking: " + request);

        var now = OffsetDateTime.now();

        // Expire stale locks
        seatLockRepository.expireHolds(SeatLockStatus.HELD.name(), SeatLockStatus.EXPIRED.name(), now);

        // Check for active locks
        var active = seatLockRepository.findActiveLocks(request.showId(), request.seats(), SeatLockStatus.HELD.name(), now);
        if (!active.isEmpty()) {
            throw new IllegalArgumentException("Some seats are already held");
        }

        var ttlSec = (request.ttlSeconds() != null ? request.ttlSeconds() : 300);
        var until = now.plusSeconds(ttlSec);

        var toPersist = new ArrayList<SeatLockEntity>(request.seats().size());

        for (var seat : request.seats()) {
            SeatLockEntity seatLockEntity = new SeatLockEntity();
            seatLockEntity.setShowId(request.showId());
            seatLockEntity.setSeatLabel(seat);
            seatLockEntity.setHoldFrom(now);
            seatLockEntity.setHoldUntil(until);
            toPersist.add(seatLockEntity);
        }

        // Create seat locks
        seatLockRepository.saveAll(toPersist);

        // Create booking

        var bookingEntity = new BookingEntity();
        bookingEntity.setShowId(request.showId());
        bookingEntity.setUserId(request.userId());
        bookingEntity.setSubtotal(request.subtotal());
        bookingEntity.setDiscountCode(request.discountCode());
        bookingEntity.setDiscount(request.discount());
        bookingEntity.setTaxesFees(request.taxesFees());
        bookingEntity.setTotal(request.total());

        var saved = bookingsRepository.save(bookingEntity);

        // Send notification publish AFTER COMMIT
        var msg = new BookingCreatedMessage(
                saved.getId(),
                saved.getUserId(),
                saved.getShowId(),
                request.seats(),
                saved.getTotal(),
                LocalDateTime.now()
        );

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                notificationPublisher.publishBookingCreated(msg);
            }
        });

        return BookingResponse.fromEntity(saved);
    }
}
