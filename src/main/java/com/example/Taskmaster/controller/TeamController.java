package com.example.Taskmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

import com.example.Taskmaster.model.User;
import com.example.Taskmaster.model.TeamUser;
import com.example.Taskmaster.dto.TeamDto;
import com.example.Taskmaster.dto.TeamInviteDto;
import com.example.Taskmaster.model.Team;
import com.example.Taskmaster.repository.TeamRepository;
import com.example.Taskmaster.repository.UserRepository;
import com.example.Taskmaster.repository.TeamUserRepository;

@Controller
@RequestMapping("/teams")
public class TeamController {
    
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository, UserRepository userRepository, TeamUserRepository teamUserRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamUserRepository = teamUserRepository;
    }

    @GetMapping("/manage")
    public String manageTeams(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));

        List<TeamUser> userTeams = teamUserRepository.findByUser(user);
        
        model.addAttribute("userTeams", userTeams);
        model.addAttribute("teamDto", new TeamDto());
        model.addAttribute("teamInviteDto", new TeamInviteDto());

        return "teams-manage";
    }

    @PostMapping("/manage")
    public String createTeam(@Valid @ModelAttribute("teamDto") TeamDto teamDto, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "teams-manage";
        }
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        
        Team newTeam = new Team();
        newTeam.setName(teamDto.getName());
        teamRepository.save(newTeam);

        TeamUser teamUser = new TeamUser();
        teamUser.setTeam(newTeam);
        teamUser.setUser(user);
        teamUser.setIsAdmin(1);
        teamUserRepository.save(teamUser);

        return "redirect:/teams/manage?success";
    }

    @PostMapping("/manage/add-member")
    public String inviteToTeam(@Valid @ModelAttribute("teamInviteDto") TeamInviteDto teamInviteDto, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "teams-manage";
        }
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamRepository.findById(teamInviteDto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));
        User invitee = userRepository.findByUsername(teamInviteDto.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        TeamUser teamUser = new TeamUser();
        teamUser.setTeam(team);
        teamUser.setUser(invitee);
        teamUserRepository.save(teamUser);

        return "redirect:/teams/manage?success";
    }
}