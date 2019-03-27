package com.tpark.back.model;


import lombok.Data;

@Data
public class Group {
    int id;
    String name;
    int course_id;
    String start_date;
    String description;
    int curr_unit;

}
