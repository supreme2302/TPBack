package com.tpark.back.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

@Data
public class School {
    private int id;
    private String name;
    private String dev_id;
    private Integer admin;
}
