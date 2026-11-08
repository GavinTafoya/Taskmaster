package com.example.Taskmaster.repository;

import com.example.Taskmaster.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    
}
