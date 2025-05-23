package co.edu.poli.finalprojectsoftware;

import co.edu.poli.finalprojectsoftware.application.service.AnswerService;
import co.edu.poli.finalprojectsoftware.domain.model.*;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.*;
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
class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private SurveyResponseRepository surveyResponseRepository;

    @InjectMocks
    private AnswerService answerService;

    private UUID surveyResponseId;
    private UUID questionId;
    private UUID optionId;
    private SurveyResponse surveyResponse;
    private Question question;

    @BeforeEach
    void setUp() {
        surveyResponseId = UUID.randomUUID();
        questionId = UUID.randomUUID();
        optionId = UUID.randomUUID();

        User respondent = new User("Test User", "test@example.com", "password");
        Survey survey = new Survey("Test Survey", respondent);
        surveyResponse = new SurveyResponse(respondent, survey);

        question = new Question("Test Question", QuestionType.SINGLE_CHOICE, survey);
        question.setId(questionId);
    }

    @Test
    void shouldSaveTextAnswerSuccessfully() {
        when(surveyResponseRepository.findById(surveyResponseId)).thenReturn(Optional.of(surveyResponse));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(answerRepository.save(any(Answer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        question.setQuestionType(QuestionType.TEXT);
        Answer answer = answerService.saveAnswer(surveyResponseId, questionId, "Test Answer", null);

        assertNotNull(answer);
        assertEquals("Test Answer", answer.getAnswerText());
        assertEquals(question, answer.getQuestion());
        assertEquals(surveyResponse, answer.getSurveyResponse());
        verify(answerRepository, times(1)).save(any(Answer.class));
    }

    @Test
    void shouldSaveOptionAnswerSuccessfully() {
        when(surveyResponseRepository.findById(surveyResponseId)).thenReturn(Optional.of(surveyResponse));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // Crear una opción con un ID asignado
        Option option = new Option("Test Option", question);
        option.setId(optionId); // Asignar el ID
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        when(answerRepository.save(any(Answer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Answer answer = answerService.saveAnswer(surveyResponseId, questionId, null, optionId);

        assertNotNull(answer);
        assertEquals(optionId, answer.getSelectedOption().getId()); // Ahora debería coincidir
        assertEquals(question, answer.getQuestion());
        assertEquals(surveyResponse, answer.getSurveyResponse());
        verify(answerRepository, times(1)).save(any(Answer.class));
    }

    @Test
    void shouldThrowExceptionWhenSurveyResponseNotFound() {
        when(surveyResponseRepository.findById(surveyResponseId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            answerService.saveAnswer(surveyResponseId, questionId, "Test Answer", null);
        });

        assertEquals("Respuesta de encuesta no encontrada", exception.getMessage());
        verify(answerRepository, never()).save(any(Answer.class));
    }

    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        when(surveyResponseRepository.findById(surveyResponseId)).thenReturn(Optional.of(surveyResponse));
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            answerService.saveAnswer(surveyResponseId, questionId, "Test Answer", null);
        });

        assertEquals("Pregunta no encontrada", exception.getMessage());
        verify(answerRepository, never()).save(any(Answer.class));
    }

    @Test
    void shouldThrowExceptionWhenOptionNotFound() {
        when(surveyResponseRepository.findById(surveyResponseId)).thenReturn(Optional.of(surveyResponse));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            answerService.saveAnswer(surveyResponseId, questionId, null, optionId);
        });

        assertEquals("Opción no encontrada", exception.getMessage());
        verify(answerRepository, never()).save(any(Answer.class));
    }
}