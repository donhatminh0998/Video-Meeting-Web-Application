package com.example.Video.Meeting.Web.Application.user;

import com.example.Video.Meeting.Web.Application.common.exception.GlobalException;
import com.example.Video.Meeting.Web.Application.jwt.JwtTokenUtils;
import com.example.Video.Meeting.Web.Application.jwt.dto.JwtRequestDto;
import com.example.Video.Meeting.Web.Application.jwt.dto.JwtResponseDto;
import com.example.Video.Meeting.Web.Application.user.dto.PasswordDto;
import com.example.Video.Meeting.Web.Application.user.dto.UpdateUserDto;
import com.example.Video.Meeting.Web.Application.user.dto.UserCreateDto;
import com.example.Video.Meeting.Web.Application.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    JwtTokenUtils tokenUtils;

    // create a new user
    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody
            UserCreateDto dto
    ) {
        try {
            UserDto createdUser = service.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sign up successful. User created with email: " + createdUser.getEmail());
        } catch (GlobalException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sign up failed: " + e.getMessage());
        }
    }

    //verify email to upgrade role
    @PostMapping("/signup/verify")
    public ResponseEntity<String> verifyEmail (
            @RequestParam("email")
            String email,
            @RequestParam("code")
            String code
    ){
        service.verifyEmail(email, code);
        return ResponseEntity.ok("Verified successfully");
    }

    // log in
    @PostMapping("/login")
    public JwtResponseDto login (
            @RequestBody
            JwtRequestDto dto
    ){
        return service.login(dto);
    }

    // change pw
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword (
            @RequestBody()
            PasswordDto dto
    ){
        service.changePassword(dto);
        return ResponseEntity.ok("Change password successful");
    }

    //Update user profile
    @PostMapping("/update-profile")
    public ResponseEntity<UserDto> updateUserProfile(
            @RequestBody UpdateUserDto dto
    ) {
        try {
            UserDto updatedUser = service.updateUserProfile(dto);
            return ResponseEntity.ok(updatedUser); // 200 OK with updated user data
        } catch (Exception e) {
            // Handle exceptions and return appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);// 500 Internal Server Error if something goes wrong
        }
    }

    @PostMapping("/upload-profile-image")
    public ResponseEntity<String> uploadProfileImage (
            @RequestParam("image")
            MultipartFile image
    )throws Exception {
        service.uploadProfileImage(image);
        return ResponseEntity.ok("upload image profile successful");
    }

    @GetMapping("/get-my-profile")
    public UserDto getMyProfile() {
        return service.getMyProfile();
    }

    @GetMapping("/validate")
    public String validateTest(
            @RequestParam("token")
            String token
    ) {
        if (!tokenUtils.validate(token))
            return "not valid jwt";
        return "valid jwt";
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<UserDto> getUserInfo() {
        UserDto userDto = service.getCurrentUserInfo();
        return ResponseEntity.ok(userDto);
    }

}