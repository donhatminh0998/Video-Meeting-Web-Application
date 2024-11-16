package com.example.Video.Meeting.Web.Application.security.config;

import com.example.Video.Meeting.Web.Application.security.service.CustomUserDetailsService;
import com.example.Video.Meeting.Web.Application.user.entity.UserEntity;
import com.example.Video.Meeting.Web.Application.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor

public class AuthenticationFacade {

    @Autowired
    private CustomUserDetailsService userService;
    private UserRepository repository;

    public UserEntity getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return userService.findByUsername(customUserDetails.getUsername());
        }
        throw new UsernameNotFoundException("User not found");
    }
}
