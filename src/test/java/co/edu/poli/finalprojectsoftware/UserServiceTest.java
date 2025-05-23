package co.edu.poli.finalprojectsoftware;

import co.edu.poli.finalprojectsoftware.application.service.UserService;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "test@example.com", "hashedPassword");
    }

    @Test
    void shouldRegisterUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser("Test User", "test@example.com", "hashedPassword");

        assertNotNull(registeredUser);
        assertEquals("Test User", registeredUser.getName());
        assertEquals("test@example.com", registeredUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldLoginUserSuccessfully() {
        when(userRepository.findByEmailAndPasswordHash("test@example.com", "hashedPassword"))
                .thenReturn(Optional.of(user));

        Optional<User> loggedInUser = userService.loginUser("test@example.com", "hashedPassword");

        assertTrue(loggedInUser.isPresent());
        assertEquals("Test User", loggedInUser.get().getName());
        verify(userRepository, times(1))
                .findByEmailAndPasswordHash("test@example.com", "hashedPassword");
    }

    @Test
    void shouldFailLoginWhenUserNotFound() {
        when(userRepository.findByEmailAndPasswordHash("test@example.com", "wrongPassword"))
                .thenReturn(Optional.empty());

        Optional<User> loggedInUser = userService.loginUser("test@example.com", "wrongPassword");

        assertFalse(loggedInUser.isPresent());
        verify(userRepository, times(1))
                .findByEmailAndPasswordHash("test@example.com", "wrongPassword");
    }
}