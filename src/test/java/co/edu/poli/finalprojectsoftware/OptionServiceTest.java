package co.edu.poli.finalprojectsoftware;

import co.edu.poli.finalprojectsoftware.application.service.OptionService;
import co.edu.poli.finalprojectsoftware.domain.model.Option;
import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.OptionRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private OptionService optionService;

    private UUID questionId;
    private UUID userId;
    private Question question;
    private Survey survey;

    @BeforeEach
    void setUp() {
        questionId = UUID.randomUUID();
        userId = UUID.randomUUID();

        // Crear un usuario y asignarlo como creador
        User creator = new User("Test User", "test@example.com", "password");
        creator.setId(userId);

        survey = new Survey("Test Survey", creator);
        survey.setId(UUID.randomUUID()); // Asignar un ID al Survey

        question = new Question("Test Question", QuestionType.SINGLE_CHOICE, survey);
        question.setId(questionId);
    }

    @Test
    void shouldAddOptionSuccessfully() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Option option = optionService.addOption(questionId, "Test Option", userId);

        assertNotNull(option);
        assertEquals("Test Option", option.getText());
        assertEquals(question, option.getQuestion());
        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            optionService.addOption(questionId, "Test Option", userId);
        });

        assertEquals("Pregunta no encontrada", exception.getMessage());
        verify(optionRepository, never()).save(any(Option.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotAuthorized() {
        UUID anotherUserId = UUID.randomUUID();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            optionService.addOption(questionId, "Test Option", anotherUserId);
        });

        assertEquals("No tienes permiso para agregar opciones a esta pregunta", exception.getMessage());
        verify(optionRepository, never()).save(any(Option.class));
    }

    @Test
    void shouldThrowExceptionWhenQuestionTypeIsText() {
        question.setQuestionType(QuestionType.TEXT);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            optionService.addOption(questionId, "Test Option", userId);
        });

        assertEquals("No se pueden agregar opciones a preguntas de tipo TEXTO", exception.getMessage());
        verify(optionRepository, never()).save(any(Option.class));
    }

    @Test
    void shouldGetSurveyIdByQuestionId() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        UUID surveyId = optionService.getSurveyIdByQuestionId(questionId);

        assertNotNull(surveyId);
        assertEquals(survey.getId(), surveyId);
        verify(questionRepository, times(1)).findById(questionId);
    }

    @Test
    void shouldThrowExceptionWhenGettingSurveyIdAndQuestionNotFound() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            optionService.getSurveyIdByQuestionId(questionId);
        });

        assertEquals("Pregunta no encontrada", exception.getMessage());
        verify(questionRepository, times(1)).findById(questionId);
    }
}
