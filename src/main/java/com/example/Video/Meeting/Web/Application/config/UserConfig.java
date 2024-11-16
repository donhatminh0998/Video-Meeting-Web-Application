package com.example.Video.Meeting.Web.Application.config;

import com.example.Video.Meeting.Web.Application.enums.UserRole;
import com.example.Video.Meeting.Web.Application.user.entity.UserEntity;
import com.example.Video.Meeting.Web.Application.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            //create admin user
            if (!userRepository.existsByUsername("admin")) {
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_ADMIN)
                        .emailVerified(true)
                        .build();
                userRepository.save(admin);
            }
            // user test
            if (!userRepository.existsByUsername("userTest1")) {
                UserEntity user1 = UserEntity.builder()
                        .username("userTest1")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_USER)
                        .email("usertest1@example.com")
                        .emailVerified(true)
                        .emailVerified(true)
                        .build();
                userRepository.save(user1);
            }
            if (!userRepository.existsByUsername("userTest2")) {
                UserEntity user2 = UserEntity.builder()
                        .username("userTest2")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_USER)
                        .email("usertest2@example.com")
                        .emailVerified(true)
                        .emailVerified(true)
                        .build();
                userRepository.save(user2);
            }
        };
    }
}
