package be_healthy_v1.controllers;

import be_healthy_v1.Application;
import be_healthy_v1.dto.ExerciseDto;
import be_healthy_v1.dto.ExerciseSetDto;
import be_healthy_v1.dto.ExerciseSetLogDto;
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
public class ExerciseSetLogControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ExerciseSetLogRepository exerciseSetLogRepository;
    @Autowired
    ExerciseSetRepository exerciseSetRepository;

    ExerciseSetEntity exerciseSetEntity;
    ExerciseSetLogEntity exerciseSetLogEntity;

    @Before
    public void setUp(){
        exerciseSetLogRepository.deleteAll();
        exerciseSetRepository.deleteAll();

        exerciseSetEntity = new ExerciseSetEntity("Set Test");
        exerciseSetLogEntity = new ExerciseSetLogEntity(new Date(), 5);

        exerciseSetRepository.save(exerciseSetEntity);
        exerciseSetLogRepository.save(exerciseSetLogEntity);
    }

    @Test
    public void getExerciseSetLogs() throws Exception {
        mvc.perform(get("/exerciseSetLogs")
                .param("pageNum", 1 + "")
                .param("size", 10 + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[*].rating").value(hasItem(exerciseSetLogEntity.getRating())));
    }

    @Test
    public void getExerciseSetLog() throws Exception {
        mvc.perform(get("/exerciseSetLogs/1")
                .param("pageNum", 1 + "")
                .param("size", 10 + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(exerciseSetLogEntity.getRating()));
    }

    @Test
    public  void addExerciseSetLogCreate() throws Exception{
        ExerciseSetLogDto exerciseSetLogDto = new ExerciseSetLogDto(new Date(), 5);

        mvc.perform(post("/exerciseSetLogs").
                contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(exerciseSetLogDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addExerciseSetLogNotFound() throws Exception {
        mvc.perform(post("/exerciseSetLogs/1/exerciseSet/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addExerciseSetConflict() throws Exception {
        exerciseSetEntity.getExerciseSetLogs().add(exerciseSetLogEntity);
        exerciseSetRepository.save(exerciseSetEntity);

        mvc.perform(post("/exerciseSetLogs/1/exerciseSet/1"))
                .andExpect(status().isConflict());
    }

    @Test
    public void deleteExerciseSetLogOk() throws Exception {
        mvc.perform(delete("/exerciseSetLogs/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteExerciseSetLogNotFound() throws Exception {
        mvc.perform(delete("/exerciseSetLogs/3"))
                .andExpect(status().isNotFound());
    }
}
