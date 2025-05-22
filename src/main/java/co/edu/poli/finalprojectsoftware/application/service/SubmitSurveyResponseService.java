package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.*;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class SubmitSurveyResponseService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public void saveResponses(UUID surveyId, UUID userId, Map<String, String> answers) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
        User respondent = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        SurveyResponse response = new SurveyResponse(respondent, survey);
        surveyResponseRepository.save(response);

        answers.forEach((rawQuestionId, answerValue) -> {
            try {
                // Extraer el UUID de la clave
                String questionId = rawQuestionId.replace("answers[", "").replace("]", "");
                UUID questionUUID = UUID.fromString(questionId);

                Question question = questionRepository.findById(questionUUID)
                        .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

                Answer answer;
                if (question.getQuestionType() == QuestionType.TEXT) {
                    if (answerValue == null || answerValue.trim().isEmpty()) {
                        throw new RuntimeException("La respuesta para la pregunta de tipo TEXTO no puede estar vacía.");
                    }
                    answer = new Answer(answerValue, response, question, null);
                } else {
                    UUID optionUUID = UUID.fromString(answerValue);
                    Option selectedOption = optionRepository.findById(optionUUID)
                            .orElseThrow(() -> new RuntimeException("Opción no encontrada"));
                    answer = new Answer(null, response, question, selectedOption);
                }
                answerRepository.save(answer);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("El ID proporcionado no es un UUID válido: " + rawQuestionId + " o " + answerValue, e);
            }
        });
    }
}