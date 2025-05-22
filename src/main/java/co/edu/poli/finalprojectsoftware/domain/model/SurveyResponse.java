package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"respondent", "survey", "answers"})
@Entity
@Table(name = "\"SurveyResponse\"")
public class SurveyResponse {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @CreationTimestamp
    @Column(name = "submittedAt", updatable = false)
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id", nullable = false)
    private User respondent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Answer> answers;

    public SurveyResponse(User respondent, Survey survey) {
        this.respondent = respondent;
        this.survey = survey;
    }

    public SurveyResponse() {
    }

    public SurveyResponse(UUID id, LocalDateTime submittedAt, Survey survey, User respondent, Set<Answer> answers) {
        this.id = id;
        this.submittedAt = submittedAt;
        this.survey = survey;
        this.respondent = respondent;
        this.answers = answers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getRespondent() {
        return respondent;
    }

    public void setRespondent(User respondent) {
        this.respondent = respondent;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}