package com.example.demo.Service;

import com.example.demo.Controller.dto.UserRegistrationDto;
import com.example.demo.DomainModel.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
}