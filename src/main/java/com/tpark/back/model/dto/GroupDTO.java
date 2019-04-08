package com.tpark.back.model.dto;


import lombok.Data;

@Data
public class GroupDTO {
    int id;
    String name;
    int course_id;
    String start_date;
    String description;
    int curr_unit;

}
