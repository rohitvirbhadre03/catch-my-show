package com.cms.notification;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendBookingEmail(Long userId, Long bookingId, Long showId,
                                 List<String> seats, Integer total, LocalDateTime createdAt) {
        System.out.printf("[EMAIL] user=%d booking=%d show=%d seats=%s total=%d at=%s%n",
                userId, bookingId, showId, seats, total, createdAt);
    }
}
