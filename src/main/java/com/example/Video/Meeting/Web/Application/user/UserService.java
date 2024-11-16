package com.example.Video.Meeting.Web.Application.user;

import com.example.Video.Meeting.Web.Application.common.exception.GlobalErrorCode;
import com.example.Video.Meeting.Web.Application.common.exception.GlobalException;
import com.example.Video.Meeting.Web.Application.common.file.FileService;
import com.example.Video.Meeting.Web.Application.enums.UserRole;
import com.example.Video.Meeting.Web.Application.enums.VerificationStatus;
import com.example.Video.Meeting.Web.Application.jwt.JwtTokenUtils;
import com.example.Video.Meeting.Web.Application.jwt.dto.JwtRequestDto;
import com.example.Video.Meeting.Web.Application.jwt.dto.JwtResponseDto;
import com.example.Video.Meeting.Web.Application.security.config.AuthenticationFacade;
import com.example.Video.Meeting.Web.Application.security.config.CustomUserDetails;
import com.example.Video.Meeting.Web.Application.user.dto.PasswordDto;
import com.example.Video.Meeting.Web.Application.user.dto.UpdateUserDto;
import com.example.Video.Meeting.Web.Application.user.dto.UserCreateDto;
import com.example.Video.Meeting.Web.Application.user.dto.UserDto;
import com.example.Video.Meeting.Web.Application.user.entity.EmailVerification;
import com.example.Video.Meeting.Web.Application.user.entity.UserEntity;
import com.example.Video.Meeting.Web.Application.user.repo.UserRepository;
import com.example.Video.Meeting.Web.Application.user.repo.VerificationRepository;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationFacade facade;
    private final VerificationRepository verificationRepository;
    private final JavaMailSender javaMailSender;
    private final FileService fileService;

    @Transactional
    public UserDto createUser(UserCreateDto dto){
        //check if email is existing
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new GlobalException(GlobalErrorCode.EMAIL_ALREADY_EXISTS);

        //Check pass
        if (!dto.getPassword().equals(dto.getPassCheck()))
            throw new GlobalException(GlobalErrorCode.PASSWORD_MISMATCH);

        // Check username
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new GlobalException(GlobalErrorCode.USERNAME_ALREADY_EXISTS);
        //Create a new user with role ROLE_USER
        UserEntity newUser = UserEntity.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(UserRole.ROLE_USER)
                .emailVerified(false)
                .build();
        // save new user
        userRepository.save(newUser);
        // send verify email
        sendVerifyCode(dto.getEmail());

        return UserDto.fromEntity(userRepository.save(newUser));
    }

    //Send verify code
    @Value("${SMTP_USERNAME}")
    private String senderEmail;

    @Transactional
    public void sendVerifyCode(String receiverEmail) {
        String verifyCode = generateRandomNumber(6);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(senderEmail);
            message.setRecipients(Message.RecipientType.TO, receiverEmail);

            message.setSubject("[Video Meeting Web Application] 인증 코드입니다.");

            StringBuilder textBody = new StringBuilder();
            textBody.append("<div style=\"max-width: 600px;\">")
                    .append("   <h2>Email Verification</h2>")
                    .append("   <p>아래의 인증 코드를 사용하여 이메일 주소를 인증해주세요.</p>")
                    .append("   <p><strong>인증 코드:</strong> <span style=\"font-size: 18px; font-weight: bold;\">")
                    .append(verifyCode)
                    .append("</span></p>")
                    .append("   <p>이 코드는 5분간 유효합니다.</p>")
                    .append("   <p>감사합니다.</p>")
                    .append("</div>");
            message.setText(textBody.toString(), "UTF-8", "HTML");

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode.EMAIL_SENDING_FAILED);
        }

        // Delete the existing verify code sending history
        verificationRepository.deleteByEmail(receiverEmail);

        EmailVerification verification = EmailVerification.builder()
                .email(receiverEmail)
                .verifyCode(verifyCode)
                .status(VerificationStatus.SENT)
                .build();

        // Save authentication code sending history
        verificationRepository.save(verification);
    }
    // Method to generate random number code
    public String generateRandomNumber(int len) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        EmailVerification verification = verificationRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.VERIFICATION_NOT_FOUND));

        if (!verification.getVerifyCode().equals(code)) {
            // Check if the verify code sent to user email matches the verify code stored in the DB.
            throw new GlobalException(GlobalErrorCode.VERIFICATION_CODE_MISMATCH);
        } else if (!verification.getStatus().equals(VerificationStatus.SENT)) {
            // If verify code is not appropriate
            throw new GlobalException(GlobalErrorCode.VERIFICATION_INVALID_STATUS);
        }
        verification.setStatus(VerificationStatus.VERIFIED);
        log.info("Verifying email: {}", email);
        log.info("Old Status: {}", verification.getStatus());
        // find user by email and upgrade role from INACTIVE to ROLE_USER
        UserEntity user = userRepository.findUserByEmail(email).orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
        user.setRole(UserRole.ROLE_USER);
        user.setEmailVerified(true);
        userRepository.save(user);
        verificationRepository.deleteByEmail(email);
    }

    // log in
    // Generate json web token (jwt)
    public JwtResponseDto login(JwtRequestDto dto){
        UserEntity userEntity = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(
                dto.getPassword(),
                userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        String jwt = jwtTokenUtils.generateToken(CustomUserDetails.fromEntity(userEntity));
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        log.info("User: {} has successfully logged in", userEntity.getUsername());
        return response;
    }

    // Change password method
    @Transactional
    public ResponseEntity<String> changePassword(PasswordDto dto) {
        UserEntity currentUser= facade.getCurrentUserEntity();

        //Check if password matches
        if (!passwordEncoder.matches(dto.getCurrentPassword(), currentUser.getPassword())) {
            throw new GlobalException(GlobalErrorCode.PASSWORD_MISMATCH);
        }

        currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(currentUser);

        return ResponseEntity.ok("{}");
    }

    // upload profile img
    public void uploadProfileImage(MultipartFile image) throws Exception {
        UserEntity currentUser = facade.getCurrentUserEntity();
        //check if user is existing
        Optional<UserEntity> userOpt = userRepository.findUserById(currentUser.getId());
        if (userOpt.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.USER_MISMATCH);
        }
        //Determine the upload directory for the profile image
        String userImgDir = "media/imgProfiles/" + currentUser.getId() + "/"; // media/imgProfile/{UserId}

        // Check and create the profile image folder if it doesn't exist
        try {
            Files.createDirectories(Path.of(userImgDir));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating profile directory");
        }

        // Delete existing profile image
        String oldProfile = currentUser.getProfileImgPath();
        if (oldProfile != null) {
            try {
                fileService.deleteFile(userImgDir + oldProfile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Save img by FileService and get saved name
        String savedFileName = fileService.uploadFile(userImgDir, image.getOriginalFilename(), image.getBytes());
        String reqPath = "/static/imgProfiles/" + currentUser.getId() + "/" + savedFileName;
        currentUser.setProfileImgPath(reqPath);
        userRepository.save(currentUser);
    }

    // User update profile info
    @Transactional
    public UserDto updateUserProfile(UpdateUserDto dto) {
        UserEntity currentUser = facade.getCurrentUserEntity();
        currentUser.setName(dto.getName());
        currentUser.setNickname(dto.getNickname());
        currentUser.setAge(dto.getAge());
        currentUser.setPhone(dto.getPhone());
        return UserDto.fromEntity(userRepository.save(currentUser));
    }

    //View Profile
    public UserDto getMyProfile() {
        UserEntity user = facade.getCurrentUserEntity();
        return UserDto.fromEntity(user);
    }

    public UserDto getCurrentUserInfo() {
        UserEntity currentUser = facade.getCurrentUserEntity();
        return UserDto.fromEntity(currentUser);
    }

}
