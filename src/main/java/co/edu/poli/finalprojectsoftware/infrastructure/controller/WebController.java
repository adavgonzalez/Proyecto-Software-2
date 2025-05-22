package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.repository.SurveyRepository;
import co.edu.poli.finalprojectsoftware.domain.repository.QuestionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class WebController {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;

    public WebController(SurveyRepository surveyRepository, QuestionRepository questionRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "Login";
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("userName", "Usuario"); // Cambiar por el nombre real del usuario
        return "Home";
    }

    @GetMapping("/createSurvey")
    public String createSurveyPage(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        return "CreateSurvey";
    }

    @GetMapping("/mySurveys")
    public String mySurveysPage(Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("surveys", surveyRepository.findByCreatorId(userId));
        return "MySurveys";
    }

    @GetMapping("/addQuestion/{surveyId}")
    public String addQuestionPage(@PathVariable UUID surveyId, Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("surveyId", surveyId);
        return "AddQuestion";
    }

    @GetMapping("/viewQuestions/{surveyId}")
    public String viewQuestionsPage(@PathVariable UUID surveyId, Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("questions", questionRepository.findBySurveyId(surveyId));
        return "ViewQuestions";
    }

    @GetMapping("/respondSurvey/{surveyId}")
    public String respondSurveyPage(@PathVariable UUID surveyId, Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
        if (survey.getCreator().getId().equals(userId)) {
            return "redirect:/otherSurveys";
        }
        model.addAttribute("survey", survey);
        model.addAttribute("questions", questionRepository.findBySurveyId(surveyId));
        return "RespondSurvey";
    }

}