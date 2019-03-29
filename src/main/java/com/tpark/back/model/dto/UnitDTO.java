package com.tpark.back.model.dto;

import lombok.Data;

@Data
public class UnitDTO {
    Integer id;
    Integer position;
    Integer course_id;
    String unit_name;
    String description;
}
