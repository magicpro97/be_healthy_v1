package be_healthy_v1.controllers;

import be_healthy_v1.Application;
import be_healthy_v1.dto.ExerciseDto;
import be_healthy_v1.entities.ExerciseEntity;
import be_healthy_v1.entities.ExerciseSetEntity;
import be_healthy_v1.respotories.ExerciseRepository;
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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mvc;
    private ExerciseEntity exerciseEntity;
    private ExerciseSetEntity exerciseSetEntity;

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ExerciseSetRepository exerciseSetRepository;


    @Before
    public void setUp(){
        exerciseRepository.deleteAll();
        exerciseSetRepository.deleteAll();

        exerciseEntity = new ExerciseEntity("Ex 1", "TEst");
        exerciseSetEntity = new ExerciseSetEntity("Set 1");

        exerciseSetRepository.save(exerciseSetEntity);
        exerciseRepository.save(exerciseEntity);
    }

    @Test
    public void getExercises() throws Exception {
        mvc.perform(get("/exercises")
                .param("pageNum", 1 + "")
                .param("size", 10 + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[*].title").value(hasItem(exerciseEntity.getTitle())))
                .andExpect(jsonPath("$.content.[*].description").value(hasItem(exerciseEntity.getDescription())));
    }


    @Test
    public void addExerciseCreate() throws Exception {
        ExerciseDto exercise = new ExerciseDto("Ex 2", "TEst");

        mvc.perform(post("/exercises").
                contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(exercise)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addExerciseNull() throws Exception {
        ExerciseDto exerciseDto = null;
        mvc.perform(post("/exercises")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(exerciseDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addExerciseSetConflict() throws Exception {
        exerciseSetEntity.getExercises().add(exerciseEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        mvc.perform(post("/exercises/1/exerciseSet/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict());
    }

    @Test
    public void addExerciseNotFound() throws Exception {
        exerciseSetEntity.getExercises().add(exerciseEntity);
        exerciseSetRepository.save(exerciseSetEntity);

        mvc.perform(post("/exercises/2/exerciseSet/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteExerciseOk() throws Exception {
        mvc.perform(delete("/exercises/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNotFound() throws Exception {
        exerciseRepository.deleteAll();
        mvc.perform(delete("/exercises/1"))
                .andExpect(status().isNotFound());
    }
}
