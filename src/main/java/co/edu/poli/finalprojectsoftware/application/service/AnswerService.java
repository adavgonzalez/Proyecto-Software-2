package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.Answer;
import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.SurveyResponse;
import co.edu.poli.finalprojectsoftware.domain.model.Option;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.AnswerRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.QuestionRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.OptionRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyResponseRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    public AnswerService(AnswerRepository answerRepository,
                         QuestionRepository questionRepository,
                         OptionRepository optionRepository,
                         SurveyResponseRepository surveyResponseRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.surveyResponseRepository = surveyResponseRepository;
    }

    public Answer saveAnswer(UUID surveyResponseId, UUID questionId, String answerText, UUID optionId) {
        SurveyResponse surveyResponse = surveyResponseRepository.findById(surveyResponseId)
                .orElseThrow(() -> new RuntimeException("Respuesta de encuesta no encontrada"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        if (question.getQuestionType() == QuestionType.TEXT) {
            if (answerText == null || answerText.trim().isEmpty()) {
                throw new RuntimeException("La respuesta de tipo TEXTO no puede estar vacía.");
            }
            return answerRepository.save(new Answer(answerText, surveyResponse, question, null));
        } else {
            Option selectedOption = optionRepository.findById(optionId)
                    .orElseThrow(() -> new RuntimeException("Opción no encontrada"));
            return answerRepository.save(new Answer(null, surveyResponse, question, selectedOption));
        }
    }
}
