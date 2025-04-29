package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String passwordHash;

    public User() {
        this.id = UUID.randomUUID();
    }

    // Getters y Setters
}
