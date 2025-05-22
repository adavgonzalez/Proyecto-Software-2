package co.edu.poli.finalprojectsoftware.domain.model;

import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"survey", "options", "answers"})
@Entity
@Table(name = "\"Question\"")
public class Question {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Lob // Para textos largos
    @Column(name = "text", nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "questionType", nullable = false)
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Option> options;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Answer> answers; // Una pregunta puede tener muchas respuestas (de diferentes SurveyResponse)

    public Question(String text, QuestionType questionType, Survey survey) {
        this.text = text;
        this.questionType = questionType;
        this.survey = survey;
    }

    public Question(UUID id, String text, QuestionType questionType, Survey survey, Set<Option> options, Set<Answer> answers) {
        this.id = id;
        this.text = text;
        this.questionType = questionType;
        this.survey = survey;
        this.options = options;
        this.answers = answers;
    }

    public Question(String text, QuestionType questionType, Survey survey, Set<Option> options) {
        this.text = text;
        this.questionType = questionType;
        this.survey = survey;
        this.options = options;
    }

    public Question() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}