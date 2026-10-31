package com.example.Taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
public class UserProfileDto {
    
    private String firstName;
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please Provide a valid email address")
    private String emailAddress;
}
