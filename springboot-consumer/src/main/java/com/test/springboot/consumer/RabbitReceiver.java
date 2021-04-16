package com.test.springboot.consumer;

import com.rabbitmq.client.Channel;
import com.test.springboot.entity.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitReceiver {

    /*@RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "order-queue", durable = "true"),
                    exchange = @Exchange(name = "order-exchange", durable = "true", type = "topic"),
                    key = "order.*"
            )
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 消费者操作
        System.out.println("------收到消息，开始消费------");
        System.out.println("订单ID:" + order.getId());

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //ACK
        channel.basicAck(deliveryTag, false);
    }*/
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
            durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
            durable = "${spring.rabbitmq.listener.order.exchange.durable}",
            type = "${spring.rabbitmq.listener.order.exchange.type}",
            ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.ignoreDeclarationException}"),
            key = "${spring.rabbitmq.listener.order.key}"
        )
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        System.out.println("----------收到消息，开始消费----------");
        System.out.println("消费端order: " + order.getId());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 手工ACK
        channel.basicAck(deliveryTag, false);
    }
}
