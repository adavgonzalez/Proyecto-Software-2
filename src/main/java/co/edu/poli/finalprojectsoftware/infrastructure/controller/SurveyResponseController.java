package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.SurveyResponseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/surveys")
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;

    public SurveyResponseController(SurveyResponseService surveyResponseService) {
        this.surveyResponseService = surveyResponseService;
    }

    @PostMapping("/{surveyId}/submitResponse")
    public String submitSurveyResponse(
            @PathVariable UUID surveyId,
            @RequestParam Map<String, String> answers,
            HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        surveyResponseService.saveSurveyResponse(surveyId, userId, answers);
        return "redirect:/otherSurveys";
    }

    @GetMapping("/mySurveys/{surveyId}/responses")
    public String viewSurveyResponses(@PathVariable UUID surveyId, Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("responses", surveyResponseService.getSurveyResponsesBySurveyId(surveyId));
        model.addAttribute("survey", surveyResponseService.getSurveyById(surveyId));
        return "ViewSurveyResponses";
    }
}
