package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService {

    @Autowired
    private UserRepository userRepository;

    public User login(String email, String passwordHash) {
        return userRepository.findByEmailAndPasswordHash(email, passwordHash)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }
}