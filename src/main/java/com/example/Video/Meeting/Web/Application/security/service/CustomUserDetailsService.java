package com.example.Video.Meeting.Web.Application.security.service;

import com.example.Video.Meeting.Web.Application.enums.UserRole;
import com.example.Video.Meeting.Web.Application.security.config.CustomUserDetails;
import com.example.Video.Meeting.Web.Application.user.entity.UserEntity;
import com.example.Video.Meeting.Web.Application.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("loadUserByUsername in UserService");
        Optional<UserEntity> optionalUser =
                userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    public UserEntity findByUsername(String username){
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();
            return userEntity;
        }
        return null;
    }

    public void createUserByOAuth2(String username, String password, String passCheck) {
        if (userExists(username) || !password.equals(passCheck))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(UserRole.ROLE_USER);
        userRepository.save(newUser);
    }


    public boolean userExists(String username) {

        return userRepository.existsByUsername(username);
    }

}
