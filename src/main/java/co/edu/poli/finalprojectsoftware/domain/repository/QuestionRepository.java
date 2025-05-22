package co.edu.poli.finalprojectsoftware.domain.repository;

import co.edu.poli.finalprojectsoftware.domain.model.Question;
import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findById(UUID questionId);
    List<Question> findBySurveyId(UUID surveyId);
}
