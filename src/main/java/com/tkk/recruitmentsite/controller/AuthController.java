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
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestParam String username,
                                         @RequestParam String email,
                                         @RequestParam String password) {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(username);
        registerUserDto.setEmail(email);
        registerUserDto.setPassword(password);
        User registerUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto, Model model) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpireIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
