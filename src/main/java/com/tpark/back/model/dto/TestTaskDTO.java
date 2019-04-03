package com.tpark.back.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TestTaskDTO {
    String question;
    String imagesrc;
    List<String> answers;
}
