package com.tpark.back.model.dto;


import lombok.Data;

@Data
public class TaskDTO {
    Integer id;
    String task_ref;
    String description;
    Integer task_type;
}
