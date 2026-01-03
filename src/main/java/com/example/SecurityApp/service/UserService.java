package com.example.SecurityApp.service;

import com.example.SecurityApp.dto.SignUpDto;
import com.example.SecurityApp.dto.UserDto;
import com.example.SecurityApp.entites.User;
import com.example.SecurityApp.exception.ResourceNotFoundException;
import com.example.SecurityApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("User with email "+username+" not found"));
    }


    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User Id "+userId+"Not found"));
    }
}
