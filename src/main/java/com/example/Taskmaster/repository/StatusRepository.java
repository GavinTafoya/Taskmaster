package com.example.Taskmaster.repository;

import com.example.Taskmaster.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    
}
