package com.mydeseret.mydeseret.service.messaging;

import com.mydeseret.mydeseret.config.RabbitMQConfig;
import com.mydeseret.mydeseret.dto.event.EmailMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void queueEmail(String to, String subject, String body) {
        EmailMessageDto message = new EmailMessageDto(to, subject, body);
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_INTERNAL,
            RabbitMQConfig.ROUTING_KEY_EMAIL,
            message
        );
        System.out.println("Queued email for: " + to);
    }
}