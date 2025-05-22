package co.edu.poli.finalprojectsoftware.infrastructure.controller;

import co.edu.poli.finalprojectsoftware.application.service.OptionService;
import co.edu.poli.finalprojectsoftware.application.service.QuestionService;
import co.edu.poli.finalprojectsoftware.application.service.CreateSurveyService;
import co.edu.poli.finalprojectsoftware.application.service.SubmitSurveyResponseService;
import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.SurveyResponse;
import co.edu.poli.finalprojectsoftware.domain.model.User;
import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import co.edu.poli.finalprojectsoftware.domain.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/surveys")
public class SurveyController {

    private final CreateSurveyService createSurveyService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final SubmitSurveyResponseService submitSurveyResponseService;
    private final SurveyResponseRepository surveyResponseRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public SurveyController(CreateSurveyService createSurveyService,
                            QuestionService questionService,
                            OptionService optionService,
                            QuestionRepository questionRepository,
                            SurveyRepository surveyRepository,
                            SubmitSurveyResponseService submitSurveyResponseService,
                            SurveyResponseRepository surveyResponseRepository,
                            UserRepository userRepository,
                            AnswerRepository answerRepository) {
        this.createSurveyService = createSurveyService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
        this.submitSurveyResponseService = submitSurveyResponseService;
        this.surveyResponseRepository = surveyResponseRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    @PostMapping("/create")
    public String createSurvey(@RequestParam String title, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        createSurveyService.createSurvey(title, userId.toString());
        return "redirect:/mySurveys";
    }

    @PostMapping("/{surveyId}/addQuestion")
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

    @PostMapping("/{questionId}/addOption")
    public String addOption(
            @PathVariable UUID questionId,
            @RequestParam String text,
            HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        optionService.addOption(questionId, text, userId);
        UUID surveyId = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"))
                .getSurvey()
                .getId();
        return "redirect:/viewQuestions/" + surveyId;
    }

    @GetMapping("/otherSurveys")
    public String getOtherSurveys(HttpSession session, Model model) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("surveys", surveyRepository.findByCreatorIdNot(userId));
        return "OtherSurveys"; // Asegúrate de que esta plantilla exista
    }

    @PostMapping("/{surveyId}/submitResponse")
    public String submitSurveyResponse(
            @PathVariable UUID surveyId,
            @RequestParam Map<String, String> answers,
            HttpSession session,
            Model model) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<SurveyResponse> responses = surveyResponseRepository.findBySurveyIdAndRespondentId(surveyId, userId);
        SurveyResponse response;
        if (responses.isEmpty()) {
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
            User respondent = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            response = surveyResponseRepository.save(new SurveyResponse(respondent, survey));
        } else {
            response = responses.get(0); // Usa el primer resultado
        }

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            String rawQuestionId = entry.getKey();
            String answerValue = entry.getValue();
            String questionId = rawQuestionId.replace("answers[", "").replace("]", "");
            UUID questionUUID = UUID.fromString(questionId);

            if (answerRepository.findBySurveyResponseIdAndQuestionId(response.getId(), questionUUID).isPresent()) {
                model.addAttribute("errorMessage", "Ya has respondido esta pregunta.");
                model.addAttribute("survey", surveyRepository.findById(surveyId).orElseThrow());
                model.addAttribute("questions", questionRepository.findBySurveyId(surveyId));
                return "RespondSurvey"; // Redirige a la página de la encuesta con el mensaje de error
            }
        }

        submitSurveyResponseService.saveResponses(surveyId, userId, answers);
        return "redirect:/api/surveys/otherSurveys";
    }

    @GetMapping("/mySurveys/{surveyId}/responses")
    public String viewSurveyResponses(
            @PathVariable UUID surveyId,
            HttpSession session,
            Model model) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));

        if (!survey.getCreator().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para ver las respuestas de esta encuesta.");
        }

        List<SurveyResponse> responses = surveyResponseRepository.findBySurveyId(surveyId);
        model.addAttribute("survey", survey);
        model.addAttribute("responses", responses);
        return "ViewSurveyResponses";
    }
}