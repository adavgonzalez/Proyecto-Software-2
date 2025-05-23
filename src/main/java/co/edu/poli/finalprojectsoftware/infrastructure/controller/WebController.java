package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.SurveyService;
import co.edu.poli.finalprojectsoftware.application.service.QuestionService;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class WebController {

    private final SurveyService surveyService;
    private final QuestionService questionService;

    public WebController(SurveyService surveyService, QuestionService questionService) {
        this.surveyService = surveyService;
        this.questionService = questionService;
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
        String userName = (String) session.getAttribute("userName");
        System.out.println("userName en sesión: " + userName); // Log para depuración
        model.addAttribute("userName", userName);
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
        model.addAttribute("surveys", surveyService.getSurveysByUser(userId));
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
        model.addAttribute("questions", questionService.getQuestionsBySurvey(surveyId));
        return "ViewQuestions";
    }

    @GetMapping("/respondSurvey/{surveyId}")
    public String respondSurveyPage(@PathVariable UUID surveyId, Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("survey", surveyService.getSurveyForResponse(surveyId, userId));
        model.addAttribute("questions", questionService.getQuestionsBySurvey(surveyId));
        return "RespondSurvey";
    }

    @GetMapping("/otherSurveys")
    public String getOtherSurveys(HttpSession session, Model model) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("surveys", surveyService.getSurveysNotCreatedByUser(userId));
        return "OtherSurveys";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        return "redirect:/login"; // Redirige al login
    }
}