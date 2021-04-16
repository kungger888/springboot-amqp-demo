package com.test.springboot.task;

import com.test.springboot.constant.Constants;
import com.test.springboot.entity.BrokerMessageLog;
import com.test.springboot.entity.Order;
import com.test.springboot.mapper.BrokerMessageLogMapper;
import com.test.springboot.producer.RabbitOrderSender;
import com.test.springboot.utils.FastJsonConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RetryMessageTasker {

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 3000, fixedDelay = 10000)
    public void reSend() {
        System.out.println("---------------定时任务开始---------------");

        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        list.forEach(messageLog -> {
            System.out.println(messageLog);
            if(messageLog.getTryCount() >= 3){
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE, new Date());
            } else {
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(), new Date());
                Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(messageLog.getMessage(), Order.class);
                try {
                    rabbitOrderSender.send(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("---------------异常处理---------------");
                }
            }
                }

        );
    }
}
