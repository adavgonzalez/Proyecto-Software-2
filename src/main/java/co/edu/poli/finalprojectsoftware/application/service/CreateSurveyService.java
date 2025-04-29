package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateSurveyService {

    private final SurveyRepository surveyRepository;

    public CreateSurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Survey createSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }
}
