package com.mydeseret.mydeseret.service.messaging;

import com.mydeseret.mydeseret.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StockAlertConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "stock-alerts")
    public void handleStockAlert(Map<String, Object> message) {
        String email = (String) message.get("email");
        String itemName = (String) message.get("itemName");
        Integer currentStock = (Integer) message.get("currentStock");

        String subject = "Low Stock Alert: " + itemName;
        String body = "Warning: Stock for item '" + itemName + "' has dropped to " + currentStock + ".\n" +
                "Please reorder immediately.";

        emailService.sendEmail(email, subject, body);
        System.out.println("Processed Stock Alert for: " + itemName);
    }
}
