package com.glide.springcloud.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE_NORMAL = "queueNormal";
    public static final String QUEUE_SMS = "queueSms";
    public static final String QUEUE_DL = "dlq";
    public static final String EXCHANGE_NORMAL = "normalx";
    public static final String EXCHANGE_DL = "dlx";
    //    public static final String ROUTING_KEY_DL = "Registration";
    // normal binding and dlx binding share same routing key
    public static final String ROUTING_KEY_NORMAL = "normal";
    public static final String ROUTING_KEY_DL = "dl";
    public static final String ROUTING_KEY_LOGIN = "Login";

    // Create normal queue with dlx and dlx routingKey
    @Bean(QUEUE_NORMAL)
    public Queue queueMail() {
        return QueueBuilder.durable(QUEUE_NORMAL).deadLetterExchange(EXCHANGE_DL).deadLetterRoutingKey(ROUTING_KEY_DL).build();
    }

    @Bean(QUEUE_SMS)
    public Queue queueSms() {
        return QueueBuilder.durable(QUEUE_SMS).build();
    }

    @Bean(QUEUE_DL)
    public Queue queueDL() {
        return new Queue(QUEUE_DL, true); // durable queue
    }

    @Bean(EXCHANGE_NORMAL)
    public Exchange exchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NORMAL).build();
    }

    @Bean(EXCHANGE_DL)
    public Exchange exchangeDL() {
        return ExchangeBuilder.directExchange(EXCHANGE_DL).build();
    }

    @Bean
    public Binding bindingReg(@Qualifier(EXCHANGE_NORMAL) DirectExchange exchange, @Qualifier(QUEUE_NORMAL) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_NORMAL);
    }

    @Bean
    public Binding bindingSms(@Qualifier(EXCHANGE_NORMAL) DirectExchange exchange, @Qualifier(QUEUE_SMS) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_LOGIN);
    }

    @Bean
    public Binding bindingDL(@Qualifier(EXCHANGE_DL) DirectExchange exchange, @Qualifier(QUEUE_DL) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_DL);
    }
}