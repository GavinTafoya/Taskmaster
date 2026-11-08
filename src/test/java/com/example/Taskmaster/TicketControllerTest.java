package com.example.Taskmaster;

import com.example.Taskmaster.controller.TicketController;
import com.example.Taskmaster.model.Status;
import com.example.Taskmaster.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private TicketRepository ticketRepository;
    @MockBean private TicketTypeRepository ticketTypeRepository;
    @MockBean private StatusRepository statusRepository;
    @MockBean private TeamRepository teamRepository;
    @MockBean private UserRepository userRepository;

    @Test
    @WithMockUser
    public void testCreateTicket_ValidationErrors() throws Exception {
        mockMvc.perform(post("/tickets/new").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets-new"))
                .andExpect(model().attributeHasFieldErrors("ticketDto", "title", "description", "ticketTypeId", "teamId", "assigneeId", "dueDate"));
    }

    @Test
    @WithMockUser
    public void testCreateTicket_Success() throws Exception {
        // Test 2: Post with valid data and verify successful save and redirect
        when(statusRepository.findById(1L)).thenReturn(Optional.of(new Status()));

        mockMvc.perform(post("/tickets/new").with(csrf())
                .param("title", "Valid Title")
                .param("description", "Valid Description")
                .param("ticketTypeId", "1")
                .param("teamId", "1")
                .param("assigneeId", "1")
                .param("dueDate", "2024-12-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/new?success"));
    }
}