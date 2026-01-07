package com.example.SecurityApp.service;

import com.example.SecurityApp.dto.LoginDto;
import com.example.SecurityApp.dto.LoginResponseDto;
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
    private final UserService userService;


    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRepository userRepository,UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
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

    public LoginResponseDto login(LoginDto loginDto) {
System.out.println(loginDto);
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
                );
        System.out.println(authentication);
        User user = (User) authentication.getPrincipal();
        System.out.println(user);
        String accesstoken = jwtService.generateAccessKey(user);
        System.out.println(accesstoken);
        String refreshToken = jwtService.generateRefreshKey(user);
        System.out.println(refreshToken);
        return new LoginResponseDto(user.getId(),accesstoken,refreshToken);
    }

    public LoginResponseDto  refreshToken(String refreshToken){
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);
        String accessToken = jwtService.generateAccessKey(user);
        return new LoginResponseDto(user.getId(),accessToken,refreshToken);
    }
}
