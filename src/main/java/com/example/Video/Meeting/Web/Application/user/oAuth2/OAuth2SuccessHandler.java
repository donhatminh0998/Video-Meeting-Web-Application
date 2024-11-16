package com.example.Video.Meeting.Web.Application.user.oAuth2;

import com.example.Video.Meeting.Web.Application.jwt.JwtTokenUtils;
import com.example.Video.Meeting.Web.Application.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtTokenUtils tokenUtils;
}