package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.OptionService;
import co.edu.poli.finalprojectsoftware.domain.model.Option;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{questionId}/add")
    public String addOption(
            @PathVariable UUID questionId,
            @RequestParam String text,
            HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        // Agregar la opción a la pregunta
        optionService.addOption(questionId, text, userId);

        // Obtener el surveyId asociado a la pregunta
        UUID surveyId = optionService.getSurveyIdByQuestionId(questionId);

        // Redirigir a la vista de preguntas de la encuesta
        return "redirect:/viewQuestions/" + surveyId;
    }
}
