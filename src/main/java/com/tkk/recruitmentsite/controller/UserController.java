package com.tkk.recruitmentsite.controller;

import com.tkk.recruitmentsite.entities.User;
import com.tkk.recruitmentsite.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    // service for handling user-related opertions
    private final UserService userService;

    // constructor to inject the userService dependecy
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // End point to retrieve the uthenticated user's details
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        // Retrieves the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Gets the authenticated user
        User currentUser = (User) authentication.getPrincipal();
        // Returns the authenticated user as a response
        return ResponseEntity.ok(currentUser);
    }

    // Endpoint to retrieve all users
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
