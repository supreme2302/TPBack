package com.tpark.back.model.dto;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
}
