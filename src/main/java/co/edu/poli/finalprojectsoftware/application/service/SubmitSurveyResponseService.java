package co.edu.poli.finalprojectsoftware.application.service;

import co.edu.poli.finalprojectsoftware.domain.model.SurveyResponse;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyResponseRepository;
import org.springframework.stereotype.Service;

@Service
public class SubmitSurveyResponseService {

    private final SurveyResponseRepository surveyResponseRepository;

    public SubmitSurveyResponseService(SurveyResponseRepository surveyResponseRepository) {
        this.surveyResponseRepository = surveyResponseRepository;
    }

    public SurveyResponse submitResponse(SurveyResponse response) {
        return surveyResponseRepository.save(response);
    }
}
