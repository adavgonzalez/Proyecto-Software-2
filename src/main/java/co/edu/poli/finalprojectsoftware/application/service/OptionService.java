package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.Option;
import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.OptionRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;

    public OptionService(OptionRepository optionRepository, QuestionRepository questionRepository) {
        this.optionRepository = optionRepository;
        this.questionRepository = questionRepository;
    }

    public Option addOption(UUID questionId, String text, UUID userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        if (!question.getSurvey().getCreator().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para agregar opciones a esta pregunta");
        }

        if (question.getQuestionType() == QuestionType.TEXT) {
            throw new RuntimeException("No se pueden agregar opciones a preguntas de tipo TEXTO");
        }

        Option option = new Option(text, question);
        return optionRepository.save(option);
    }

    public UUID getSurveyIdByQuestionId(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"))
                .getSurvey()
                .getId();
    }
}