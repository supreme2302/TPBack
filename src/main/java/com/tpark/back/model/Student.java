package com.tpark.back.model;
import lombok.Data;

@Data
public class Student {
    private int id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private String group_id;
    private String phone;
    private int school_id;
}
