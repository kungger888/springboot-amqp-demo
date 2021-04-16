package com.test.springboot.mapper;

import com.test.springboot.entity.Order;
import org.springframework.stereotype.Component;

@Component
public interface OrderMapper {
    void insert(Order order);
}
