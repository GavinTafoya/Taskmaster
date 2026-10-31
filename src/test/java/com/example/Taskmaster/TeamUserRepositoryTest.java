package com.example.Taskmaster;

import com.example.Taskmaster.model.Team;
import com.example.Taskmaster.model.TeamUser;
import com.example.Taskmaster.model.User;
import com.example.Taskmaster.repository.TeamRepository;
import com.example.Taskmaster.repository.TeamUserRepository;
import com.example.Taskmaster.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TeamUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @BeforeEach
    void setUp() {
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateTeamAndAssignAdmin() {
        // 1. Create User
        User user = new User();
        user.setUsername("teamadmin");
        user.setPassword("pass");
        user.setEmailAddress("admin@team.com");
        userRepository.save(user);

        // 2. Create Team
        Team team = new Team();
        team.setName("Development Team");
        teamRepository.save(team);

        // 3. Create TeamUser (Link User and Team as Admin)
        TeamUser teamUser = new TeamUser();
        teamUser.setUser(user);
        teamUser.setTeam(team);
        teamUser.setIsAdmin(1);
        teamUserRepository.save(teamUser);

        // 4. Verify Link
        List<TeamUser> userTeams = teamUserRepository.findByUser(user);
        assertThat(userTeams).hasSize(1);
        
        TeamUser savedLink = userTeams.get(0);
        assertThat(savedLink.getTeam().getName()).isEqualTo("Development Team");
        assertThat(savedLink.getIsAdmin()).isEqualTo(1);
        assertThat(savedLink.getUser().getUsername()).isEqualTo("teamadmin");
    }

    @Test
    void testMultipleMembersInTeamConstraints() {
        // Create Admin User
        User adminUser = new User();
        adminUser.setUsername("admin2");
        adminUser.setEmailAddress("admin2@example.com");
        userRepository.save(adminUser);

        // Create Standard User
        User standardUser = new User();
        standardUser.setUsername("member1");
        standardUser.setEmailAddress("member1@example.com");
        userRepository.save(standardUser);

        // Create Team
        Team team = new Team();
        team.setName("QA Team");
        teamRepository.save(team);

        // Link Admin (isAdmin = 1)
        TeamUser adminLink = new TeamUser();
        adminLink.setUser(adminUser);
        adminLink.setTeam(team);
        adminLink.setIsAdmin(1);
        teamUserRepository.save(adminLink);

        // Link Member (isAdmin = 0)
        TeamUser memberLink = new TeamUser();
        memberLink.setUser(standardUser);
        memberLink.setTeam(team);
        memberLink.setIsAdmin(0);
        teamUserRepository.save(memberLink);

        // Verify Admin Constraints
        List<TeamUser> adminTeams = teamUserRepository.findByUser(adminUser);
        assertThat(adminTeams).hasSize(1);
        assertThat(adminTeams.get(0).getIsAdmin()).isEqualTo(1);
        assertThat(adminTeams.get(0).getTeam().getName()).isEqualTo("QA Team");

        // Verify Standard Member Constraints
        List<TeamUser> memberTeams = teamUserRepository.findByUser(standardUser);
        assertThat(memberTeams).hasSize(1);
        assertThat(memberTeams.get(0).getIsAdmin()).isEqualTo(0);
        assertThat(memberTeams.get(0).getTeam().getName()).isEqualTo("QA Team");
    }
}
