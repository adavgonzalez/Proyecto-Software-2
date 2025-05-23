package co.edu.poli.finalprojectsoftware.domain.repository;

import co.edu.poli.finalprojectsoftware.domain.model.Survey;
import co.edu.poli.finalprojectsoftware.domain.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findBySurveyIdAndRespondentId(UUID surveyId, UUID respondentId);
    List<SurveyResponse> findBySurveyId(UUID surveyId);
    Optional<SurveyResponse> findById(UUID surveyResponseId);

}
