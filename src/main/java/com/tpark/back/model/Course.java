package com.tpark.back.model;

import lombok.Data;

@Data
public class Course {
    int id;
    String name;
    String description;
    int schoolId;
}
