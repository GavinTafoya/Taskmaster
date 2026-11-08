package com.example.Taskmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;

import com.example.Taskmaster.dto.TicketDto;
import com.example.Taskmaster.model.Ticket;
import com.example.Taskmaster.model.Status;
import com.example.Taskmaster.repository.StatusRepository;
import com.example.Taskmaster.repository.TeamRepository;
import com.example.Taskmaster.repository.TicketRepository;
import com.example.Taskmaster.repository.TicketTypeRepository;
import com.example.Taskmaster.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final StatusRepository statusRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;



    @GetMapping("/new")
    public String showCreateTicketForm(Model model) {
        model.addAttribute("ticketDto", new TicketDto());

        model.addAttribute("ticketTypes", ticketTypeRepository.findAll());
        model.addAttribute("teams", teamRepository.findAll());
        model.addAttribute("users", userRepository.findAll());

        return "tickets-new";
    }

    @PostMapping("/new")
    public String createTicket(@Valid @ModelAttribute("ticketDto") TicketDto ticketDto, BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            // Re-inject dropdown options so the form renders correctly with errors
            model.addAttribute("ticketTypes", ticketTypeRepository.findAll());
            model.addAttribute("teams", teamRepository.findAll());
            model.addAttribute("users", userRepository.findAll());
            return "tickets-new";
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setDueDate(ticketDto.getDueDate());

        ticket.setTicketType(ticketTypeRepository.findById(ticketDto.getTicketTypeId()).orElse(null));
        ticket.setTeam(teamRepository.findById(ticketDto.getTeamId()).orElse(null));
        ticket.setAssignee(userRepository.findById(ticketDto.getAssigneeId()).orElse(null));
        
        if (ticketDto.getReviewerId() != null) {
            ticket.setReviewer(userRepository.findById(ticketDto.getReviewerId()).orElse(null));
        }

        // Set default status to Backlog (assuming ID 1)
        Status backlogStatus = statusRepository.findById((long) 1).orElseThrow(() -> new RuntimeException("Backlog status not found"));
        ticket.setStatus(backlogStatus);

        ticketRepository.save(ticket);

        return "redirect:/tickets/new?success";
    }
}
