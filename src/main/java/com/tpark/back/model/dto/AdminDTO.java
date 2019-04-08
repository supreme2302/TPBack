package com.tpark.back.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.Transient;

public class AdminDTO {
    private int id;
    private String email;
    private String password;
    private String schoolName;

    public AdminDTO() {}

    @JsonCreator
    public AdminDTO(
            @JsonProperty("id") int id,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("schoolName") String schoolName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.schoolName = schoolName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
