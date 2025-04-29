package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "survey_response")
public class SurveyResponse {
    @Id
    private UUID id;

    @ManyToOne
    private Survey survey;

    @ManyToOne
    private User respondent;

    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL)
    private List<Answer> answers;

    public SurveyResponse() {
        this.id = UUID.randomUUID();
        this.submittedAt = LocalDateTime.now();
    }

    // Getters y Setters
}
