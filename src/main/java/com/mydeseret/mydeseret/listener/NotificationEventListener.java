package com.mydeseret.mydeseret.listener;

import com.mydeseret.mydeseret.event.SaleCreatedEvent;
import com.mydeseret.mydeseret.service.messaging.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    @Autowired 
    private NotificationProducer notificationProducer;

    @Async 
    @EventListener
    public void handleSaleNotification(SaleCreatedEvent event) {
        var sale = event.getSale();
        
        if (sale.getCustomer() != null && sale.getCustomer().getEmail() != null) {
            String subject = "Receipt for Order " + sale.getReceiptNumber();
            String body = "Thank you for your purchase of " + sale.getTotalAmount();
            
            notificationProducer.queueEmail(sale.getCustomer().getEmail(), subject, body);
        }

        if (sale.getTotalAmount().doubleValue() > 10000) {
            System.out.println("$$$ HUGE SALE ALERT: " + sale.getTotalAmount());
        }
    }
}