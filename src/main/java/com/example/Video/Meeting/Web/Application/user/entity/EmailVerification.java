package com.example.Video.Meeting.Web.Application.user.entity;

import com.example.Video.Meeting.Web.Application.common.base.BaseEntity;
import com.example.Video.Meeting.Web.Application.enums.VerificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_verification")
public class EmailVerification extends BaseEntity {
    private String email;
    private String verifyCode;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status; //SENT, VERIFIED


}

