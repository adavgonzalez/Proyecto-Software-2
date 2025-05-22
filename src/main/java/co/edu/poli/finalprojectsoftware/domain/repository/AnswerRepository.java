package co.edu.poli.finalprojectsoftware.domain.repository;

import co.edu.poli.finalprojectsoftware.domain.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findBySurveyResponseIdAndQuestionId(UUID surveyResponseId, UUID questionId);
}