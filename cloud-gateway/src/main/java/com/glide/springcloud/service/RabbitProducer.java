package com.glide.springcloud.service;


import com.glide.springcloud.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.glide.springcloud.config.RabbitConfig.ROUTING_KEY_NORMAL;

@Service
public class RabbitProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message, String routingKey, int ttl) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NORMAL, routingKey, message,(msg)->{
            msg.getMessageProperties().setExpiration(String.valueOf(ttl));
            return msg;
        });
    }
}
