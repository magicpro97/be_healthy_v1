package be_healthy_v1.controllers;

import be_healthy_v1.Application;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ExerciseSetLogControllerTest {

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

}
