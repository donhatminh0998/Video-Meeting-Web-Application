package com.example.Video.Meeting.Web.Application.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordDto {
    private String currentPassword;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()-_+=]{8,15}$",
            message = "비밀번호는 영어와 숫자가 포함된 8자리 이상, 15자리 이하의 문자열이어야 합니다.")
    private String newPassword;
}
