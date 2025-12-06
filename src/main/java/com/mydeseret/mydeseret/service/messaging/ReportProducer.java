package com.mydeseret.mydeseret.service.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ReportProducer {

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routingkey.reports}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ReportProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendReportRequest(UUID reportRequestId, String tenantId, String reportType,
            Map<String, Object> params) {
        Map<String, Object> message = Map.of(
                "reportRequestId", reportRequestId,
                "tenantId", tenantId,
                "reportType", reportType,
                "params", params);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
