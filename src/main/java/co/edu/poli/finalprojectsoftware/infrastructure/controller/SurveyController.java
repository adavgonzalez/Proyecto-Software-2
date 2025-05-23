package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.SurveyService;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/create")
    public String createSurvey(@RequestParam String title, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        surveyService.createSurvey(title, userId);
        return "redirect:/mySurveys";
    }

    @GetMapping("/mySurveys")
    public String getMySurveys(HttpSession session, Model model) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        List<Survey> surveys = surveyService.getSurveysByUser(userId);
        model.addAttribute("surveys", surveys);
        return "MySurveys";
    }

    @GetMapping("/otherSurveys")
    public String getOtherSurveys(HttpSession session, Model model) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        List<Survey> surveys = surveyService.getSurveysNotCreatedByUser(userId);
        model.addAttribute("surveys", surveys);
        return "OtherSurveys";
    }
}