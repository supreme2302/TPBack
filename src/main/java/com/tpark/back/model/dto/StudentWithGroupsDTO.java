package com.tpark.back.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentWithGroupsDTO {
    private int id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private List<GroupDTO> group_id;
    private String phone;
    private int school_id;
}
