package co.edu.poli.finalprojectsoftware.domain.model;

import co.edu.poli.finalprojectsoftware.domain.model.enums.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {
    @Id
    private UUID id;
    private String text;
    private int orderInSurvey;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options;

    public Question() {
        this.id = UUID.randomUUID();
    }

    // Getters y Setters
}
