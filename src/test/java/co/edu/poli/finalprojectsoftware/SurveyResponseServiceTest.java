package co.edu.poli.finalprojectsoftware;

import co.edu.poli.finalprojectsoftware.application.service.SurveyResponseService;
import co.edu.poli.finalprojectsoftware.domain.model.*;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyResponseServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SurveyResponseRepository surveyResponseRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private SurveyResponseService surveyResponseService;

    private UUID surveyId;
    private UUID userId;
    private Survey survey;
    private User respondent;

    @BeforeEach
    void setUp() {
        surveyId = UUID.randomUUID();
        userId = UUID.randomUUID();

        respondent = new User("Test User", "test@example.com", "password");
        respondent.setId(userId);

        survey = new Survey("Test Survey", respondent);
        survey.setId(surveyId);
    }

    @Test
    void shouldSaveSurveyResponseSuccessfully() {
        Map<String, String> answers = new HashMap<>();
        UUID questionId = UUID.randomUUID();
        UUID optionId = UUID.randomUUID();
        answers.put(questionId.toString(), optionId.toString());

        Question question = new Question("Test Question", QuestionType.SINGLE_CHOICE, survey);
        question.setId(questionId);

        Option option = new Option("Test Option", question);
        option.setId(optionId);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        when(userRepository.findById(userId)).thenReturn(Optional.of(respondent));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        surveyResponseService.saveSurveyResponse(surveyId, userId, answers);

        verify(surveyResponseRepository, times(1)).save(any(SurveyResponse.class));
        verify(answerRepository, times(1)).save(any(Answer.class));
    }

    @Test
    void shouldThrowExceptionWhenSurveyNotFound() {
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            surveyResponseService.saveSurveyResponse(surveyId, userId, new HashMap<>());
        });

        assertEquals("Encuesta no encontrada", exception.getMessage());
        verify(surveyResponseRepository, never()).save(any(SurveyResponse.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            surveyResponseService.saveSurveyResponse(surveyId, userId, new HashMap<>());
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(surveyResponseRepository, never()).save(any(SurveyResponse.class));
    }

    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        UUID questionId = UUID.randomUUID();
        Map<String, String> answers = new HashMap<>();
        answers.put(questionId.toString(), "Test Answer");

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        when(userRepository.findById(userId)).thenReturn(Optional.of(respondent));
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            surveyResponseService.saveSurveyResponse(surveyId, userId, answers);
        });

        assertEquals("Pregunta no encontrada", exception.getMessage());
        verify(answerRepository, never()).save(any(Answer.class));
    }

    @Test
    void shouldThrowExceptionWhenOptionNotFound() {
        UUID questionId = UUID.randomUUID();
        UUID optionId = UUID.randomUUID();
        Map<String, String> answers = new HashMap<>();
        answers.put(questionId.toString(), optionId.toString());

        Question question = new Question("Test Question", QuestionType.SINGLE_CHOICE, survey);
        question.setId(questionId);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        when(userRepository.findById(userId)).thenReturn(Optional.of(respondent));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            surveyResponseService.saveSurveyResponse(surveyId, userId, answers);
        });

        assertEquals("Opción no encontrada", exception.getMessage());
        verify(answerRepository, never()).save(any(Answer.class));
    }
}
