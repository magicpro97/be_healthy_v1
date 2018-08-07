package be_healthy_v1.respotories;

import be_healthy_v1.entities.ExerciseSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSetEntity, Long> {
}
