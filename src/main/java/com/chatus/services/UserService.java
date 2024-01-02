package com.chatus.services;

import com.chatus.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    public Optional<User> getUserByEmail(String userEmail) {
        return Optional.of(new User());
    }
}
