package co.edu.poli.finalprojectsoftware.domain.repository;

import co.edu.poli.finalprojectsoftware.domain.model.Answer;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByCreatorId(UUID creatorId);
    List<Survey> findByCreatorIdNot(UUID creatorId);
    Optional<Survey> findById(UUID surveyId);
}
