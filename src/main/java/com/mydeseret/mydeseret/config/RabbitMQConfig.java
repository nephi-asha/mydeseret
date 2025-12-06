package com.mydeseret.mydeseret.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_INTERNAL = "internal.exchange";
    public static final String QUEUE_EMAIL = "email.queue";
    public static final String ROUTING_KEY_EMAIL = "email.routing.key";

    @org.springframework.beans.factory.annotation.Value("${app.rabbitmq.queue.reports}")
    private String reportQueueName;

    @org.springframework.beans.factory.annotation.Value("${app.rabbitmq.routingkey.reports}")
    private String reportRoutingKey;

    @Bean
    public TopicExchange internalExchange() {
        return new TopicExchange(EXCHANGE_INTERNAL);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_EMAIL, true);
    }

    @Bean
    public Queue stockAlertsQueue() {
        return new Queue("stock-alerts", true);
    }

    @Bean
    public Queue reportQueue() {
        return new Queue(reportQueueName, true);
    }

    @Bean
    public Binding bindingEmail(Queue emailQueue, TopicExchange internalExchange) {
        return BindingBuilder.bind(emailQueue).to(internalExchange).with(ROUTING_KEY_EMAIL);
    }

    @Bean
    public Binding bindingReport(Queue reportQueue, TopicExchange internalExchange) {
        return BindingBuilder.bind(reportQueue).to(internalExchange).with(reportRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}