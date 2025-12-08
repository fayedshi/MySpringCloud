package com.glide.springcloud.service;

import com.glide.springcloud.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RabbitConsumer {
    private static final Log logger = LogFactory.getLog(RabbitConsumer.class);

    @RabbitListener(queues = {RabbitConfig.QUEUE_NORMAL, RabbitConfig.QUEUE_SMS})
    public void receiveNormal(Message message, Channel channel) throws IOException {
//        System.out.println("received from normal queue: " + message);
//        channel.basicConsume()
//        channel.basicAck(deliveryTag, false);
        logger.info(String.format("Received message: %s, deliveryTag: %d \n",
                new String(message.getBody()), message.getMessageProperties().getDeliveryTag()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    @RabbitListener(queues = RabbitConfig.QUEUE_DL)
    public void handleMessage(Message message, Channel channel) throws Exception {
        try {
            logger.info("Received message from DLQ: " + message);
            // 模拟消息处理
//            processMessage(message);
            // 消息处理成功后，手动发送ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 消息处理失败，拒绝消息并决定是否重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}

