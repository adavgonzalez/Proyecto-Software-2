package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "option")
@Getter
@Setter
public class Option {
    @Id
    private UUID id;
    private String text;
    private int orderInQuestion;

    @ManyToOne
    private Question question;

    public Option() {
        this.id = UUID.randomUUID();
    }

}
