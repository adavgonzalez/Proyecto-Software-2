package co.edu.poli.finalprojectsoftware;

import co.edu.poli.finalprojectsoftware.application.service.QuestionService;
import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.QuestionRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyRepository;
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
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private QuestionService questionService;

    private UUID surveyId;
    private UUID userId;
    private Survey survey;

    @BeforeEach
    void setUp() {
        surveyId = UUID.randomUUID();
        userId = UUID.randomUUID();
        User creator = new User("Test User", "test@example.com", "password");
        creator.setId(userId);
        survey = new Survey("Test Survey", creator);
        survey.setId(surveyId);
    }

    @Test
    void shouldAddQuestionSuccessfully() {
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Question question = questionService.addQuestion(surveyId, "Test Question", QuestionType.SINGLE_CHOICE, userId);

        assertNotNull(question);
        assertEquals("Test Question", question.getText());
        assertEquals(QuestionType.SINGLE_CHOICE, question.getQuestionType());
        assertEquals(survey, question.getSurvey());
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void shouldThrowExceptionWhenSurveyNotFound() {
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionService.addQuestion(surveyId, "Test Question", QuestionType.SINGLE_CHOICE, userId);
        });

        assertEquals("Encuesta no encontrada", exception.getMessage());
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void shouldGetQuestionsBySurvey() {
        when(questionRepository.findBySurveyId(surveyId)).thenReturn(List.of(new Question("Test Question", QuestionType.TEXT, survey)));

        List<Question> questions = questionService.getQuestionsBySurvey(surveyId);

        assertNotNull(questions);
        assertEquals(1, questions.size());
        assertEquals("Test Question", questions.get(0).getText());
        verify(questionRepository, times(1)).findBySurveyId(surveyId);
    }
}
