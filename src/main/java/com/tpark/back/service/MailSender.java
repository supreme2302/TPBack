package com.tpark.back.service;

public interface MailSender {
    void send(String emailTo, String subject, String message);
}
