package com.cms.booking.notify;

import com.cms.shared.model.BookingCreatedMessage;
import com.cms.booking.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {
    private final RabbitTemplate rabbit;

    public NotificationPublisher(RabbitTemplate rabbit) { this.rabbit = rabbit; }

    public void publishBookingCreated(BookingCreatedMessage msg) {
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_BOOKING_CREATED, msg);
    }
}
