package co.edu.poli.finalprojectsoftware.domain.repository;

import co.edu.poli.finalprojectsoftware.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);
    Optional<User> findByEmail(String email);
}
