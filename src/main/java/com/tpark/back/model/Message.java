package com.tpark.back.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String message;

    public Message(Enum message) {
        this.message = message.toString();
    }

    public Message(String message) {
        this.message = message;
    }
}