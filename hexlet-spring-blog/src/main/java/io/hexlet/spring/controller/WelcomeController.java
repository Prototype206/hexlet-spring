package io.hexlet.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class WelcomeController {
    @Value("${app.welcome-message}")
    private String welcomeMessage;
    @Value("${app.admin-email}")
    private String email;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage + "/n" + email;
    }
}
