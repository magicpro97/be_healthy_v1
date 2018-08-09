package be_healthy_v1.repositories;

import be_healthy_v1.entities.ExerciseSetEntity;
import be_healthy_v1.entities.ExerciseSetLogEntity;
import be_healthy_v1.respotories.ExerciseSetLogRepository;
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
public class ExerciseSetLogRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ExerciseSetLogRepository exerciseSetLogRepository;

    ExerciseSetLogEntity exerciseSetLogEntity;
    ExerciseSetEntity exerciseSetEntity;

    @Before
    public void setUp(){
        exerciseSetLogEntity = new ExerciseSetLogEntity(null,5);
        exerciseSetEntity = new ExerciseSetEntity("Set 1");
        entityManager.persist(exerciseSetLogEntity);
        entityManager.persist(exerciseSetEntity);
        entityManager.flush();
    }

    @Test
    public void findAll(){
        PageRequest pageRequest = new PageRequest(0,10);
        Page<ExerciseSetLogEntity> page = exerciseSetLogRepository.findAll(pageRequest);
        assertEquals(10,page.getSize());
    }

    @Test
    public void findAllByESId(){
        PageRequest pageRequest = new PageRequest(0,10);
        Page<ExerciseSetLogEntity> page = exerciseSetLogRepository.findAllByESId(pageRequest,Long.parseLong("1"));
        assertEquals(0,page.getNumberOfElements());
    }

}
