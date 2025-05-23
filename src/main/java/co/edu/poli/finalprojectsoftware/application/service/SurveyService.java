package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    public SurveyService(SurveyRepository surveyRepository,
                         UserRepository userRepository) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
    }

    public List<Survey> getSurveysNotCreatedByUser(UUID userId) {
        return surveyRepository.findByCreatorIdNot(userId);
    }

    public List<Survey> getSurveysByUser(UUID userId) {
        return surveyRepository.findByCreatorId(userId);
    }

    public Survey getSurveyForResponse(UUID surveyId, UUID userId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
    }

    public Survey createSurvey(String title, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Survey survey = new Survey(title, creator);
        return surveyRepository.save(survey);
    }
}