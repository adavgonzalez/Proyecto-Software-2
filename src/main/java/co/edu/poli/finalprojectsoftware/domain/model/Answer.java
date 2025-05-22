package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"surveyResponse", "question", "selectedOption"})
@Entity
@Table(name = "\"Answer\"")
public class Answer {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Lob // Para textos largos
    @Column(name = "answerText") // Anulable, usado para QuestionType.TEXT
    private String answerText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_response_id", nullable = false)
    private SurveyResponse surveyResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id") // Anulable, usado para QuestionType.SINGLE_CHOICE
    private Option selectedOption;

    public Answer(String answerText, SurveyResponse surveyResponse, Question question, Option selectedOption) {
        this.answerText = answerText;
        this.surveyResponse = surveyResponse;
        this.question = question;
        this.selectedOption = selectedOption;
    }

    public Answer(UUID id, String answerText, SurveyResponse surveyResponse, Question question, Option selectedOption) {
        this.id = id;
        this.answerText = answerText;
        this.surveyResponse = surveyResponse;
        this.question = question;
        this.selectedOption = selectedOption;
    }

    public Answer() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public SurveyResponse getSurveyResponse() {
        return surveyResponse;
    }

    public void setSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponse = surveyResponse;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Option getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }
}