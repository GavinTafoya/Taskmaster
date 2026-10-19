package com.example.Taskmaster.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor 
@Data
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String username;
        private String password;

        private String firstName;
        private String lastName;
        private String emailAddress;

        private String role;
        private Long supervisorId;
}
