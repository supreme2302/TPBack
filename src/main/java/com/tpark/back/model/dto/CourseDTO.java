package com.tpark.back.model.dto;

import lombok.Data;

@Data
public class CourseDTO {
    int id;
    String name;
    String description;
    int schoolId;
}
