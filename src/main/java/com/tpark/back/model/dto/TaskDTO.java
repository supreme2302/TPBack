package com.tpark.back.model.dto;


import com.google.gson.JsonObject;
import lombok.Data;
import java.util.List;

@Data
public class TaskDTO {
    Integer id;
    String name;
    Integer task_type;
    List<Integer> unit_id;
    Object task;
}
