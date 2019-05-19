package com.tpark.back.model.dto;
import lombok.Data;

@Data
public class SchoolDTO {
    private int id;
    private String name = "School";
    private int admin;
    private String main_color= "#FFFFFF";//3F51B5
    private String secondary_color = "#AAAAAA";//303F9F
    private String language = "english";
    private String imageLink;
    private String appName = "app";
}
