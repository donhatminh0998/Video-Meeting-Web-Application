package com.example.Video.Meeting.Web.Application.user.repo;

import com.example.Video.Meeting.Web.Application.user.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<EmailVerification, Long> {
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    Optional<EmailVerification> findByEmail(String email);
}
