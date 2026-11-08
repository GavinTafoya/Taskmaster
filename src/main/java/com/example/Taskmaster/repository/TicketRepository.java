package com.example.Taskmaster.repository;

import com.example.Taskmaster.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
}
