package com.example.SecurityApp.service;

import com.example.SecurityApp.dto.LoginDto;
import com.example.SecurityApp.dto.SignUpDto;
import com.example.SecurityApp.dto.UserDto;
import com.example.SecurityApp.entites.User;
import com.example.SecurityApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;
    }

    public UserDto signUp(SignUpDto signUpDto) {

        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());

        User savedUser;
        if (user.isPresent()) {
            throw new BadCredentialsException("User with email already exist " + signUpDto.getEmail());
        } else {
            User newUserCreated = modelMapper.map(signUpDto, User.class);
            newUserCreated.setPassword(passwordEncoder.encode(newUserCreated.getPassword()));
            savedUser = userRepository.save(newUserCreated);
        }
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
                );
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateKey(user);
        return token;
    }
}
