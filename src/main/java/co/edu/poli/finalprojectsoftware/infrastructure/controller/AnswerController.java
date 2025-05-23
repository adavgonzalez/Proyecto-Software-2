package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.AnswerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/{surveyResponseId}/{questionId}/save")
    public String saveAnswer(
            @PathVariable UUID surveyResponseId,
            @PathVariable UUID questionId,
            @RequestParam(required = false) String answerText,
            @RequestParam(required = false) UUID optionId,
            HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        answerService.saveAnswer(surveyResponseId, questionId, answerText, optionId);
        return "redirect:/otherSurveys";
    }
}
