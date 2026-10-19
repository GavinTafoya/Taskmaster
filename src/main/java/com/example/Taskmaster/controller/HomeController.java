package com.example.Taskmaster.controller;
    
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String landingPage(Model model) {
        model.addAttribute("appStatus", "System Operational - PostgreSQL Connected");
        model.addAttribute("username", "username"); // Replace with actual logged-in username if available
        return "index";
    }
}
