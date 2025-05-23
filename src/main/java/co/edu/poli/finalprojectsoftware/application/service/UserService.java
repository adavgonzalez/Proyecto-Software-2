package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String passwordHash) {
        User user = new User(name, email, passwordHash);
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String email, String passwordHash) {
        return userRepository.findByEmailAndPasswordHash(email, passwordHash);
    }
}