package com.tpark.back.model;
import lombok.Data;

@Data
public class ChangePassword {
    private String oldPassword;
    private String newPassword;
}
