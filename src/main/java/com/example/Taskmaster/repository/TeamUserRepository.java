package com.example.Taskmaster.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Taskmaster.model.TeamUser;
import com.example.Taskmaster.model.User;
import java.util.List;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findByUser(User user);
}
