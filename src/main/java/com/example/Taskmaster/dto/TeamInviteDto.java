package com.example.Taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamInviteDto {

    @NotNull(message = "Team ID is required")
    private Long teamId;

    @NotBlank(message = "Username to invite is required")
    private String username;
}