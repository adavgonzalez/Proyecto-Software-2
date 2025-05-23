package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.*;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SurveyResponseService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final AnswerRepository answerRepository;

    public SurveyResponseService(SurveyRepository surveyRepository,
                                 UserRepository userRepository,
                                 SurveyResponseRepository surveyResponseRepository,
                                 QuestionRepository questionRepository,
                                 OptionRepository optionRepository,
                                 AnswerRepository answerRepository) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.surveyResponseRepository = surveyResponseRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.answerRepository = answerRepository;
    }

    public void saveSurveyResponse(UUID surveyId, UUID userId, Map<String, String> answers) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
        User respondent = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        SurveyResponse surveyResponse = new SurveyResponse(respondent, survey);
        surveyResponseRepository.save(surveyResponse);

        answers.forEach((questionIdStr, answerValue) -> {
            UUID questionId = UUID.fromString(questionIdStr);
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

            Answer answer;
            if (question.getQuestionType() == QuestionType.TEXT) {
                if (answerValue == null || answerValue.trim().isEmpty()) {
                    throw new RuntimeException("La respuesta de tipo TEXTO no puede estar vacía.");
                }
                answer = new Answer(answerValue, surveyResponse, question, null);
            } else {
                UUID optionId = UUID.fromString(answerValue);
                Option selectedOption = optionRepository.findById(optionId)
                        .orElseThrow(() -> new RuntimeException("Opción no encontrada"));
                answer = new Answer(null, surveyResponse, question, selectedOption);
            }
            answerRepository.save(answer);
        });
    }

    public List<SurveyResponse> getSurveyResponsesBySurveyId(UUID surveyId) {
        return surveyResponseRepository.findBySurveyId(surveyId);
    }

    public Survey getSurveyById(UUID surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
    }
}