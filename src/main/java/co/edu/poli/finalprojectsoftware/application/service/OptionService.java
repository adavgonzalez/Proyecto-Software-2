package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.Option;
import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.OptionRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuestionRepository questionRepository;

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
}