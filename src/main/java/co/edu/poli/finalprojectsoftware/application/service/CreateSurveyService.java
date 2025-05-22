package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CreateSurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    public Survey createSurvey(String title, String creatorId) {
        if (!isValidUUID(creatorId)) {
            throw new IllegalArgumentException("El ID del usuario no es un UUID válido");
        }

        User creator = userRepository.findById(UUID.fromString(creatorId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Survey survey = new Survey(title, creator);
        return surveyRepository.save(survey);
    }

    private boolean isValidUUID(String uuid) {
        String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        return Pattern.matches(uuidRegex, uuid);
    }
}