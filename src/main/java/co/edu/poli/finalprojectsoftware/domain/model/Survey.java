package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"creator", "questions", "surveyResponses"})
@Entity
@Table(name = "\"Survey\"")
public class Survey {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @CreationTimestamp // Lombok no tiene esto, es de Hibernate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Question> questions;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<SurveyResponse> surveyResponses;

    public Survey(String title, User creator) {
        this.title = title;
        this.creator = creator;
    }

    public Survey(String title, User creator, Set<Question> questions) {
        this.title = title;
        this.creator = creator;
        this.questions = questions;
    }

    public Survey(String title, User creator, Set<Question> questions, Set<SurveyResponse> surveyResponses) {
        this.title = title;
        this.creator = creator;
        this.questions = questions;
        this.surveyResponses = surveyResponses;
    }

    public Survey(String title, User creator, Set<Question> questions, Set<SurveyResponse> surveyResponses, LocalDateTime createdAt) {
        this.title = title;
        this.creator = creator;
        this.questions = questions;
        this.surveyResponses = surveyResponses;
        this.createdAt = createdAt;
    }

    public Survey(String title, User creator, LocalDateTime createdAt) {
        this.title = title;
        this.creator = creator;
        this.createdAt = createdAt;
    }



    public Survey() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    public void setSurveyResponses(Set<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
    }
}