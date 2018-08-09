package be_healthy_v1.repositories;

import be_healthy_v1.entities.ExerciseEntity;
import be_healthy_v1.entities.ExerciseSetEntity;
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
public class ExerciseRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ExerciseSetRepository exerciseSetRepository;

    ExerciseEntity exerciseEntity;
    ExerciseSetEntity exerciseSetEntity;

    @Before
    public void setUp(){
        exerciseEntity = new ExerciseEntity("Exx test", "e");
        exerciseSetEntity = new ExerciseSetEntity("Set 1");
        entityManager.persist(exerciseEntity);
        entityManager.persist(exerciseSetEntity);
        entityManager.flush();
    }

    @Test
    public void findAll(){
        PageRequest pageRequest = new PageRequest(0,10);
        Page<ExerciseSetEntity> page = exerciseSetRepository.findAll(pageRequest);
        assertEquals(10,page.getSize());
    }
}
