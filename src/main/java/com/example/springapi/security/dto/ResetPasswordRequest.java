package com.example.springapi.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest{
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}