package com.example.Video.Meeting.Web.Application.jwt.dto;

import com.example.Video.Meeting.Web.Application.enums.UserRole;
import lombok.Data;

@Data
public class JwtResponseDto {
    String token;
    private UserRole userRole;
    private String redirectUrl;
}
