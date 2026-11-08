package com.example.Taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data 
public class TicketDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Ticket type is required")
    private Long ticketTypeId;

    @NotNull(message = "Team is required")
    private Long teamId;

    @NotNull(message = "Assignee is required")
    private Long assigneeId;

    private Long reviewerId;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
