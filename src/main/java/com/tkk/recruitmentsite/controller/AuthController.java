package com.tkk.recruitmentsite.controller;

import com.tkk.recruitmentsite.dto.LoginUserDto;
import com.tkk.recruitmentsite.dto.RegisterUserDto;
import com.tkk.recruitmentsite.entities.User;
import com.tkk.recruitmentsite.response.LoginResponse;
import com.tkk.recruitmentsite.service.AuthenticationService;
import com.tkk.recruitmentsite.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // Service for handling JWT token operations
    private final JwtService jwtService;
    // Service for handling user authentication
    private final AuthenticationService authenticationService;

    // Constructor in inject dependencies
    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    // Endpoint for user registration
    // Maps POST request to /auth/signup
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        // Creates a DTO for the registration data
        RegisterUserDto registerUserDto = new RegisterUserDto();
        // sets the username
        registerUserDto.setUsername(username);
        // sets the email
        registerUserDto.setEmail(email);
        // sets the password
        registerUserDto.setPassword(password);
        // calls the service to register the user
        User registerUser = authenticationService.signup(registerUserDto);
        // returns the registered user as response
        return ResponseEntity.ok(registerUser);
    }

    // endpoint for the user authentication
    // maps POST requests to /auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto, Model model) {
        // authenticates the user
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        // generates a JWT token for the authentiated user
        String jwtToken = jwtService.generateToken(authenticatedUser);
        // creates a response object for the login response
        LoginResponse loginResponse = new LoginResponse();
        // sets the JWT token in the response
        loginResponse.setToken(jwtToken);
        // sets the token expireation time in the response
        loginResponse.setExpireIn(jwtService.getExpirationTime());

        // returns the login response with the token
        return ResponseEntity.ok(loginResponse);
    }
}
