package com.cms.notification;

import com.cms.shared.model.BookingCreatedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookingCreatedListener {

    private final EmailService emailService;

    public BookingCreatedListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "notifications.booking.created")
    public void onBookingCreated(BookingCreatedMessage msg) {
        emailService.sendBookingEmail(
                msg.userId(),
                msg.bookingId(),
                msg.showId(),
                msg.seats(),
                msg.total(),
                msg.createdAt()
        );
    }
}