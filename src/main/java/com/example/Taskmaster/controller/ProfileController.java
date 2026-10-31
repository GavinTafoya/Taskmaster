package com.example.Taskmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import java.security.Principal;

import com.example.Taskmaster.repository.UserRepository;
import com.example.Taskmaster.model.User;
import com.example.Taskmaster.dto.UserProfileDto;

@Controller 
public class ProfileController {
    
    private final UserRepository userRepository;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setEmailAddress(user.getEmailAddress());

        model.addAttribute("userProfile", userProfileDto);
        model.addAttribute("username", user.getUsername());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("userProfile") UserProfileDto userProfileDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setEmailAddress(userProfileDto.getEmailAddress());
        userRepository.save(user);
        return "redirect:/profile?success";
    }
}
