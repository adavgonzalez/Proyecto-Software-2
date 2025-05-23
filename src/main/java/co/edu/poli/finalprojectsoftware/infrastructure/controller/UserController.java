package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.UserService;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String passwordHash) {
        User user = userService.registerUser(name, email, passwordHash);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String passwordHash,
            HttpSession session) {
        return userService.loginUser(email, passwordHash)
                .map(user -> {
                    System.out.println("Nombre del usuario autenticado: " + user.getName()); // Log para depuración
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("userName", user.getName());
                    return "redirect:/home";
                })
                .orElse("redirect:/login?error=true");
    }
}