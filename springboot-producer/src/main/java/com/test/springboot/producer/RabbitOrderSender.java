package com.test.springboot.producer;

import com.test.springboot.constant.Constants;
import com.test.springboot.entity.Order;
import com.test.springboot.mapper.BrokerMessageLogMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RabbitOrderSender {

    // 自动注入RabbitTemplate模板类
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    // 回调函数
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("correlationData: " + correlationData);
            String messageId = correlationData.getId();
            if(ack){
                // 如果confirm返回成功 则进行更新
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS, new Date());
            } else {
                // 失败则进行具体的后续操作：重试或补偿等手段
                System.out.println("异常处理...");
            }
        }
    };

    public void send(Order order) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId());
        rabbitTemplate.convertAndSend(
                "order-exchange1111", // exchange
                "order.ABC", // routingKey
                order, // 消息体内容
                correlationData // correlationData 消息唯一ID
        );
    }
}
