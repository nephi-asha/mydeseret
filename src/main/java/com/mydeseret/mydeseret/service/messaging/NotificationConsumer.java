package com.mydeseret.mydeseret.service.messaging;

import com.mydeseret.mydeseret.config.RabbitMQConfig;
import com.mydeseret.mydeseret.dto.event.EmailMessageDto;
import com.mydeseret.mydeseret.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void processEmail(EmailMessageDto message) {
        try {
            System.out.println("Processing email from queue...");
            emailService.sendEmail(message.getTo(), message.getSubject(), message.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send email from queue: " + e.getMessage());
            throw new RuntimeException(e); // Just added this line for retry 
        }
    }
}