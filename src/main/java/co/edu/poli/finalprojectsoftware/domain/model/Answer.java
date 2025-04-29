package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "answer")
public class Answer {
    @Id
    private UUID id;

    @ManyToOne
    private SurveyResponse surveyResponse;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Option selectedOption;

    private String answerText;

    public Answer() {
        this.id = UUID.randomUUID();
    }

    // Getters y Setters
}
