package com.example.Video.Meeting.Web.Application.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserDto {
    private String name;
    private String nickname;
    private Integer age;
    private String phone;
}
