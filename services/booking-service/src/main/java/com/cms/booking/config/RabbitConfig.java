package com.cms.booking.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "notifications";
    public static final String ROUTING_BOOKING_CREATED = "booking.created";
    public static final String QUEUE_BOOKING_CREATED = "notifications.booking.created";

    @Bean TopicExchange notificationsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean Queue bookingCreatedQueue() {
        return QueueBuilder.durable(QUEUE_BOOKING_CREATED).build();
    }

    @Bean Binding bookingCreatedBinding() {
        return BindingBuilder.bind(bookingCreatedQueue())
                .to(notificationsExchange())
                .with(ROUTING_BOOKING_CREATED);
    }

    @Bean Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory cf) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(jackson2JsonMessageConverter());
        return tpl;
    }
}
