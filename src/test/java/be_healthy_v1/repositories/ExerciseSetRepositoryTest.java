package be_healthy_v1.repositories;

import be_healthy_v1.entities.ExerciseEntity;
import be_healthy_v1.entities.ExerciseSetEntity;
import be_healthy_v1.entities.ExerciseSetLogEntity;
import be_healthy_v1.respotories.ExerciseSetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExerciseSetRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    ExerciseSetRepository exerciseSetRepository;
    private ExerciseSetEntity exerciseSet;
    private ExerciseEntity exercise;
    private ExerciseSetLogEntity exerciseSetLog;

    @Before
    public void setUp(){
        exerciseSet = new ExerciseSetEntity("Set test");
        exercise = new ExerciseEntity("Ex xx1", "Test");
        exerciseSetLog = new ExerciseSetLogEntity(null,5);
        entityManager.persist(exerciseSet);
        entityManager.persist(exercise);
        entityManager.persist(exerciseSetLog);
        entityManager.flush();
    }

    @Test
    public void findAll(){
        PageRequest pageRequest = new PageRequest(0,10);
        Page<ExerciseSetEntity> page = exerciseSetRepository.findAll(pageRequest);
        assertEquals(10,page.getSize());
    }
}
