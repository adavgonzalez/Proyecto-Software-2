package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.QuestionService;
import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/{surveyId}/add")
    public String addQuestion(
            @PathVariable UUID surveyId,
            @RequestParam String text,
            @RequestParam QuestionType questionType,
            HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        questionService.addQuestion(surveyId, text, questionType, userId);
        return "redirect:/viewQuestions/" + surveyId;
    }

    @GetMapping("/{surveyId}")
    public String getQuestionsBySurvey(@PathVariable UUID surveyId, Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        List<Question> questions = questionService.getQuestionsBySurvey(surveyId);
        model.addAttribute("questions", questions);
        return "ViewQuestions";
    }
}
