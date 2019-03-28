package com.tpark.back.model;
import lombok.Data;

import java.util.List;

@Data
public class Student {
    private int id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private List<Integer> group_id;
    private String phone;
    private int school_id;
}
