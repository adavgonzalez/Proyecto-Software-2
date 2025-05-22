package co.edu.poli.finalprojectsoftware.domain.repository;

import co.edu.poli.finalprojectsoftware.domain.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional <Option> findById(UUID id);
}
