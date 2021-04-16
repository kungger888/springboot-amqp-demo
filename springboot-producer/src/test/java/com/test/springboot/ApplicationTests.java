package com.test.springboot;

import com.test.springboot.entity.Order;
import com.test.springboot.producer.RabbitOrderSender;
import com.test.springboot.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RabbitOrderSender orderSender;

    @Test
    public void testSend1() throws Exception {
        Order order = new Order();
        order.setId("20210415000000001");
        order.setName("测试订单1");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        orderSender.send(order);
    }

    @Autowired
    private OrderService orderService;

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId("202104150000002");
        order.setName("测试创建订单");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        orderService.createOrder(order);
    }
}
