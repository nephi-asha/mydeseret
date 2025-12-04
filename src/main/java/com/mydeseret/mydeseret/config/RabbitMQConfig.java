package com.mydeseret.mydeseret.config;

// import org.springframework.amqp.core.*;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
public class RabbitMQConfig {

    // public static final String EXCHANGE_SALES = "sales.exchange";
    // public static final String QUEUE_INVENTORY = "inventory.queue";
    // public static final String QUEUE_FINANCE = "finance.queue";
    // public static final String QUEUE_NOTIFICATION = "notification.queue";

    // @Bean
    // public TopicExchange salesExchange() {
    //     return new TopicExchange(EXCHANGE_SALES);
    // }

    // @Bean public Queue inventoryQueue() { return new Queue(QUEUE_INVENTORY, true); }
    // @Bean public Queue financeQueue() { return new Queue(QUEUE_FINANCE, true); }
    // @Bean public Queue notificationQueue() { return new Queue(QUEUE_NOTIFICATION, true); }

    // @Bean
    // public Binding bindingInventory(Queue inventoryQueue, TopicExchange salesExchange) {
    //     return BindingBuilder.bind(inventoryQueue).to(salesExchange).with("sale.created");
    // }
    // @Bean
    // public Binding bindingFinance(Queue financeQueue, TopicExchange salesExchange) {
    //     return BindingBuilder.bind(financeQueue).to(salesExchange).with("sale.created");
    // }
    // @Bean
    // public Binding bindingNotification(Queue notificationQueue, TopicExchange salesExchange) {
    //     return BindingBuilder.bind(notificationQueue).to(salesExchange).with("sale.created");
    // }

    // @Bean
    // public Jackson2JsonMessageConverter jsonMessageConverter() {
    //     return new Jackson2JsonMessageConverter();
    // }

    // @Bean
    // public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    //     RabbitTemplate template = new RabbitTemplate(connectionFactory);
    //     template.setMessageConverter(jsonMessageConverter());
    //     return template;
    // }
    
}