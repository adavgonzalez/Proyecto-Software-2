package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"question", "answersWhereSelected"})
@Entity
@Table(name = "\"Option\"")
public class Option {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Opcional: si quieres navegar desde Option a las Answers que la seleccionaron
    @OneToMany(mappedBy = "selectedOption", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) // Cuidado con CascadeType.ALL aquí
    private Set<Answer> answersWhereSelected;

    public Option(String text, Question question) {
        this.text = text;
        this.question = question;
    }

    public Option() {
    }

    public Option(UUID id, String text, Question question, Set<Answer> answersWhereSelected) {
        this.id = id;
        this.text = text;
        this.question = question;
        this.answersWhereSelected = answersWhereSelected;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<Answer> getAnswersWhereSelected() {
        return answersWhereSelected;
    }

    public void setAnswersWhereSelected(Set<Answer> answersWhereSelected) {
        this.answersWhereSelected = answersWhereSelected;
    }
}