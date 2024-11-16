package com.example.Video.Meeting.Web.Application.config;

import com.example.Video.Meeting.Web.Application.jwt.JwtTokenFilter;
import com.example.Video.Meeting.Web.Application.jwt.JwtTokenUtils;
import com.example.Video.Meeting.Web.Application.user.oAuth2.OAuth2SuccessHandler;
import com.example.Video.Meeting.Web.Application.user.oAuth2.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService service;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/views/**", "/static/img/**").permitAll();
                    auth.requestMatchers(
                                    "/users/login",
                                    "/users/signup",
                                    "/users/signup/send-code",
                                    "/users/signup/verify"
//                                    "/users/request-password-reset",
//                                    "/users/reset-password"
                            )
                            .anonymous();
                    auth.requestMatchers(
                                    "/users/get-my-profile", //view profile
                                    "/users/update-profile", //update profile info
                                    "/users/change-password", //change password
                                    "/users/upload-profile-image", //upload profile image
                                    "/users/**"
                            )
                            .authenticated();
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/error", "/static/**", "/", "/oauth2/**", "/websocket/**")
                            .permitAll();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/views/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .permitAll()
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                service
                        ),
                        AuthorizationFilter.class
                );


        return http.build();
    }
}
