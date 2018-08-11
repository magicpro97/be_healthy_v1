package be_healthy_v1.controllers;

import be_healthy_v1.Application;
import be_healthy_v1.dto.ExerciseSetDto;
import be_healthy_v1.entities.ExerciseEntity;
import be_healthy_v1.entities.ExerciseSetEntity;
import be_healthy_v1.entities.ExerciseSetLogEntity;
import be_healthy_v1.respotories.ExerciseRepository;
import be_healthy_v1.respotories.ExerciseSetLogRepository;
import be_healthy_v1.respotories.ExerciseSetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ExerciseSetControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ExerciseRepository exerciseRepository;
    @Autowired
    ExerciseSetLogRepository exerciseSetLogRepository;
    @Autowired
    ExerciseSetRepository exerciseSetRepository;

    ExerciseSetEntity  exerciseSetEntity;
    ExerciseEntity exerciseEntity;
    ExerciseSetLogEntity exerciseSetLogEntity;

    @Before
    public void setUp(){
        exerciseEntity = new ExerciseEntity("Ex1","");
        exerciseSetEntity = new ExerciseSetEntity("Set 1");
        exerciseSetLogEntity = new ExerciseSetLogEntity(new Date(), 5);

        exerciseRepository.save(exerciseEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        exerciseSetLogRepository.save(exerciseSetLogEntity);
    }

    @Test
    public void getExerciseSets() throws Exception {
        mvc.perform(get("/exerciseSets")
        .param("pageNum", 1 +"")
        .param("size", 10 +""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title").value(exerciseSetEntity.getTitle()));
    }

    @Test
    public void getExerciseSet() throws Exception {
        mvc.perform(get("/exerciseSets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(exerciseSetEntity.getTitle()));
    }

    @Test
    public void addExerciseSet() throws Exception {
        ExerciseSetDto exerciseSetDto = new ExerciseSetDto("Set x");
        mvc.perform(post("/exerciseSets")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(exerciseSetDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addExerciseCreated() throws Exception {
        mvc.perform(post("/exerciseSets/1/exercise/1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addExerciseNotFound() throws Exception {
        mvc.perform(post("/exerciseSets/1/exercise/6"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addExerciseSetLogCreated() throws Exception {
        mvc.perform(post("/exerciseSets/1/exerciseSetLog/1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addExerciseSetLogNotFound() throws Exception {
        mvc.perform(post("/exerciseSets/1/exerciseSetLog/6"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteExerciseSetOk() throws Exception {
        mvc.perform(delete("/exerciseSets/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteExerciseSetNotFound() throws Exception {
        mvc.perform(delete("/exerciseSets/5"))
                .andExpect(status().isNotFound());
    }
}
