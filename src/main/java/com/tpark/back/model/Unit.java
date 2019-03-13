package com.tpark.back.model;

import lombok.Data;

@Data
public class Unit {
    Integer id;
    Integer position;
    Integer course_id;
    String unit_name;
}
