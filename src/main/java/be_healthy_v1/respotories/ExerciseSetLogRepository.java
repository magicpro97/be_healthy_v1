package be_healthy_v1.respotories;

import be_healthy_v1.entities.ExerciseSetLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseSetLogRepository extends JpaRepository<ExerciseSetLogEntity, Long> {
    @Query(value = "select esl.id, esl.dateLog, esl.rating, es.id from ExerciseSetLogEntity esl join ExerciseSetEntity es on esl.id = es.id where es.id =:id")
    Page<ExerciseSetLogEntity> findAllByESId(Pageable pageRequest, @Param("id") Long id);
}
