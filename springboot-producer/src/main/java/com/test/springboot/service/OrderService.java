package com.test.springboot.service;

import com.test.springboot.constant.Constants;
import com.test.springboot.entity.BrokerMessageLog;
import com.test.springboot.entity.Order;
import com.test.springboot.mapper.BrokerMessageLogMapper;
import com.test.springboot.mapper.OrderMapper;
import com.test.springboot.producer.RabbitOrderSender;
import com.test.springboot.utils.FastJsonConvertUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    public void createOrder(Order order) throws Exception {
        Date orderTime = new Date();
        orderMapper.insert(order);
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        brokerMessageLog.setMessageId(order.getMessageId());
        brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(order));
        brokerMessageLog.setStatus("0");
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogMapper.insert(brokerMessageLog);

        rabbitOrderSender.send(order);
    }
}
