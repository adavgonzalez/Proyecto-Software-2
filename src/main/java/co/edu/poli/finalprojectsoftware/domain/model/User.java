package co.edu.poli.finalprojectsoftware.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Evita problemas con colecciones y relaciones bidireccionales
@ToString(exclude = {"createdSurveys", "surveyResponses"}) // Evita recursión infinita en toString
@Entity
@Table(name = "\"User\"") // Usar comillas dobles si el nombre de la tabla es una palabra reservada o contiene mayúsculas en algunos SGBD
public class User {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)") // O BINARY(16)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Survey> createdSurveys;

    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<SurveyResponse> surveyResponses;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<Survey> getCreatedSurveys() {
        return createdSurveys;
    }

    public void setCreatedSurveys(Set<Survey> createdSurveys) {
        this.createdSurveys = createdSurveys;
    }

    public Set<SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    public void setSurveyResponses(Set<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
    }

    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User() {
    }
}