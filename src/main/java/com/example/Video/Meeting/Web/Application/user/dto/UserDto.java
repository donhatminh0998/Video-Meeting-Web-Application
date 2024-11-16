package com.example.Video.Meeting.Web.Application.user.dto;

import com.example.Video.Meeting.Web.Application.enums.UserRole;
import com.example.Video.Meeting.Web.Application.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String nickname;
    private Integer age;
    private String phone;
    private String email;
    private UserRole role;
    private String profileImgPath;

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .name(entity.getName())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .role(entity.getRole())
                .profileImgPath(entity.getProfileImgPath())
                .build();
    }
}
