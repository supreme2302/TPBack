package com.tpark.back.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnitDomain {
    Integer id;
    Integer prev_pos;
    Integer next_pos;
    Integer course_id;
    String unit_name;
    String description;
    String tags;
    String status;
}
