package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.LoginUserService;
import co.edu.poli.finalprojectsoftware.application.service.RegisterUserService;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUserService registerUserService;
    private final LoginUserService loginUserService;

    public UserController(RegisterUserService registerUserService, LoginUserService loginUserService) {
        this.registerUserService = registerUserService;
        this.loginUserService = loginUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String passwordHash) {
        User user = registerUserService.register(name, email, passwordHash);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String passwordHash,
            HttpSession session) {
        User user = loginUserService.login(email, passwordHash);
        session.setAttribute("userId", user.getId());
        return "redirect:/home";
    }
}