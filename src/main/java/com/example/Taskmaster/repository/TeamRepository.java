package com.example.Taskmaster.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Taskmaster.model.Team;
import java.util.Optional;

@Repository     
public interface TeamRepository extends JpaRepository<Team, Long>{
    Optional<Team> findByName(String name);
}
