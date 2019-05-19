package com.tpark.back.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class UnitDTO {
    Integer id;
    Integer prev_pos;
    Integer next_pos;
    Integer course_id;
    String unit_name;
    String description;
    List<String> tags;
    String status;
}
