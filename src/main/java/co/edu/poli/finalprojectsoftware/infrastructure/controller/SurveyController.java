package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.CreateSurveyService;
import co.edu.poli.finalprojectsoftware.application.service.SubmitSurveyResponseService;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.SurveyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final CreateSurveyService createSurveyService;
    private final SubmitSurveyResponseService submitSurveyResponseService;

    public SurveyController(CreateSurveyService createSurveyService, SubmitSurveyResponseService submitSurveyResponseService) {
        this.createSurveyService = createSurveyService;
        this.submitSurveyResponseService = submitSurveyResponseService;
    }

    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {
        Survey createdSurvey = createSurveyService.createSurvey(survey);
        return ResponseEntity.ok(createdSurvey);
    }

    @PostMapping("/{surveyId}/responses")
    public ResponseEntity<SurveyResponse> submitResponse(@RequestBody SurveyResponse response) {
        SurveyResponse submittedResponse = submitSurveyResponseService.submitResponse(response);
        return ResponseEntity.ok(submittedResponse);
    }
}
