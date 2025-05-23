package co.edu.poli.finalprojectsoftware;

import co.edu.poli.finalprojectsoftware.application.service.SurveyService;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SurveyService surveyService;

    private UUID userId;
    private User user;
    private Survey survey;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User("Test User", "test@example.com", "password");
        user.setId(userId);

        survey = new Survey("Test Survey", user);
        survey.setId(UUID.randomUUID());
    }

    @Test
    void shouldCreateSurvey() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey createdSurvey = surveyService.createSurvey("Test Survey", userId);

        assertNotNull(createdSurvey);
        assertEquals("Test Survey", createdSurvey.getTitle());
        assertEquals(user, createdSurvey.getCreator());
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    void shouldGetSurveysByUser() {
        when(surveyRepository.findByCreatorId(userId)).thenReturn(List.of(survey));

        List<Survey> surveys = surveyService.getSurveysByUser(userId);

        assertNotNull(surveys);
        assertEquals(1, surveys.size());
        assertEquals("Test Survey", surveys.get(0).getTitle());
        verify(surveyRepository, times(1)).findByCreatorId(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            surveyService.createSurvey("Test Survey", userId);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}
