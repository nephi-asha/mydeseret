package com.mydeseret.mydeseret.service.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StockAlertProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routingkey.stock-alerts}")
    private String routingKey;

    public void sendLowStockAlert(String email, String itemName, int currentStock) {
        Map<String, Object> message = Map.of(
                "email", email,
                "itemName", itemName,
                "currentStock", currentStock);

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Sent Low Stock Alert for: " + itemName);
    }
}
