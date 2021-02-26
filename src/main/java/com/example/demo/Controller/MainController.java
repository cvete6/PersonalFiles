package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "authentication/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/persons/start";
    }
}