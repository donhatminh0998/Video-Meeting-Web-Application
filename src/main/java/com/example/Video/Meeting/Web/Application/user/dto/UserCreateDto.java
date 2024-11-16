package com.example.Video.Meeting.Web.Application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @Pattern(regexp = "^[a-zA-Z]{8,12}$", message = "유저네임은 영어로 8글자 이상, 12글자 이하여야 합니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()-_+=]{8,15}$",
            message = "비밀번호는 영어와 숫자가 포함된 8자리 이상, 15자리 이하의 문자열이어야 합니다.")
    private String password;

    private String passCheck;

    @Email(message = "유효한 이메일을 입력하세요.")
    private String email;

}
