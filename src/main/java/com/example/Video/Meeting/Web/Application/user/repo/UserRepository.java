package com.example.Video.Meeting.Web.Application.user.repo;

import com.example.Video.Meeting.Web.Application.enums.UserRole;
import com.example.Video.Meeting.Web.Application.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String admin);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserEntity> findUserByEmail(String email);

    Optional<UserEntity> findUserById(Long userId);

    List<UserEntity> findByRole(UserRole role);
}
