package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.QuestionRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public QuestionService(QuestionRepository questionRepository, SurveyRepository surveyRepository) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
    }

    public Question addQuestion(UUID surveyId, String text, QuestionType questionType, UUID userId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));

        if (!survey.getCreator().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para agregar preguntas a esta encuesta");
        }

        Question question = new Question(text, questionType, survey);
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsBySurvey(UUID surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }
}