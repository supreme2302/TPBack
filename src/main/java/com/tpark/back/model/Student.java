package com.tpark.back.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


public class Student {
    private String email;
    private String name;
    private String surname;
    private String password;
    private String group_id;
    private String phone;
    private int school_id;

    public Student() {}

    @JsonCreator
    public Student (
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("surname") String surname,
            @JsonProperty("password") String password,
            @JsonProperty("group_id") String group_id,
            @JsonProperty("phone") String phone,
            @JsonProperty("school_id") int school_id) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.group_id = group_id;
        this.phone = phone;
        this.school_id = school_id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }
}
