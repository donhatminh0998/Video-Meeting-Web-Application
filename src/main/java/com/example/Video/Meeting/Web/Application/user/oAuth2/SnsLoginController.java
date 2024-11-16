package com.example.Video.Meeting.Web.Application.user.oAuth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class SnsLoginController {
    private final OAuth2UserService oAuth2UserService; // Dịch vụ xử lý đăng nhập

    public SnsLoginController(OAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @GetMapping("login")
    public String loginForm() {
        return "login";
    }
}
