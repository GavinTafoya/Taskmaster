package com.example.Taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
public class TeamDto {
    
    @NotBlank(message = "Team name cannot be blank")
    private String name;
}
