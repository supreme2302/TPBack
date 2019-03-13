package com.tpark.back.model;


import lombok.Data;

@Data
public class Task {
    Integer id;
    String task_ref;
    String descriptiom;
    Integer task_type;
}
