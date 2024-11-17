package com.example.Video.Meeting.Web.Application.user.entity;

import com.example.Video.Meeting.Web.Application.common.base.BaseEntity;
import com.example.Video.Meeting.Web.Application.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class UserEntity extends BaseEntity {
 //   @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String nickname;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private UserRole role;

   // @Column(unique = true)
    private String email;

 //   @Column(unique = true)
    private String phone;
    private String profileImgPath;

    @Builder.Default
    private boolean emailVerified = false;

}
