package com.test.springboot.entity;

import java.io.Serializable;

public class Order implements Serializable {
    private static final long serialVersionUID = 8226955445766253434L;
    private String id;
    private String name;
    private String messageId; // 存储消息发送的唯一标识

    public Order() {
    }

    public Order(String id, String name, String messageId) {
        this.id = id;
        this.name = name;
        this.messageId = messageId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
