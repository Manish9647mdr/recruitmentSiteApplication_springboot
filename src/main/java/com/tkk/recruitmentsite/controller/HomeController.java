package com.tkk.recruitmentsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    // Serve signup page
    @GetMapping("/")
    public String getHomepage() {
        return "index";
    }

    // Serve signup page
    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    // Serve login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/recruit")
    public String recruitPage() {
        return "pages/recruit";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "pages/about";
    }

    @GetMapping("/event")
    public String eventPage() {
        return "pages/event";
    }

    @GetMapping("/content")
    public String contentPage() {
        return "pages/content";
    }
}
