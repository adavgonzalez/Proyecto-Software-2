package co.edu.poli.finalprojectsoftware.domain.repository;

import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    // Custom query methods can be defined here if needed
    // For example, to find surveys by a specific attribute
    // List<Survey> findByAttributeName(String attributeName);
}
