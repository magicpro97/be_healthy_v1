package be_healthy_v1.controllers;

import be_healthy_v1.dto.ExerciseDto;
import be_healthy_v1.dto.ExerciseSetDto;
import be_healthy_v1.dto.ExerciseSetLogDto;
import be_healthy_v1.entities.ExerciseEntity;
import be_healthy_v1.entities.ExerciseSetEntity;
import be_healthy_v1.entities.ExerciseSetLogEntity;
import be_healthy_v1.respotories.ExerciseRepository;
import be_healthy_v1.respotories.ExerciseSetLogRepository;
import be_healthy_v1.respotories.ExerciseSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/exerciseSets")
public class ExerciseSetController {
    public static final Logger logger = LoggerFactory.getLogger(ExerciseSetController.class);

    private ExerciseRepository exerciseRepository;
    private ExerciseSetRepository exerciseSetRepository;
    private ExerciseSetLogRepository exerciseSetLogRepository;

    @Autowired
    public ExerciseSetController(ExerciseRepository exerciseRepository,
                              ExerciseSetRepository exerciseSetRepository,
                              ExerciseSetLogRepository exerciseSetLogRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseSetRepository = exerciseSetRepository;
        this.exerciseSetLogRepository = exerciseSetLogRepository;
    }

    @GetMapping
    public ResponseEntity getExerciseSets(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false)Sort sort){
        if(sort == null) sort = new Sort(Sort.Direction.ASC,"id");
        PageRequest pageRequest = new PageRequest(page - 1, size,sort);
        Page<ExerciseSetEntity> exerciseSetEntityPage = exerciseSetRepository.findAll(pageRequest);
        return  new ResponseEntity<>(exerciseSetEntityPage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getExerciseSetById(@PathVariable("id") Long id){
        logger.info("Retrieve exercise set by id {}", id);
        if(!exerciseSetRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(id);
        return  new ResponseEntity<>(exerciseSetEntity,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addExerciseSet(@RequestBody ExerciseSetDto exerciseSetDto){
        logger.info("Add a new exercise set {}", exerciseSetDto);
        if(exerciseSetDto == null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        exerciseSetRepository.save(new ExerciseSetEntity(exerciseSetDto.getTitle()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/exercise/{eId}")
    public ResponseEntity addExerciseAssociate(@PathVariable("id") Long id, @PathVariable("eId") Long eId){
        if(id == null || eId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!exerciseSetRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!exerciseRepository.existsById(eId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExerciseEntity exerciseEntity = exerciseRepository.getOne(eId);
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(id);
        exerciseSetEntity.getExercises().add(exerciseEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/exerciseSetLog/{eslId}")
    public ResponseEntity addExerciseSetLogAssociate(@PathVariable("id") Long id, @PathVariable("eslId") Long eslId){
        if(id == null || eslId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!exerciseSetRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!exerciseSetLogRepository.existsById(eslId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExerciseSetLogEntity exerciseSetLogEntity = exerciseSetLogRepository.getOne(eslId);
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(id);
        exerciseSetEntity.getExerciseSetLogs().add(exerciseSetLogEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateExerciseSet(@RequestBody ExerciseSetDto exerciseSetDto){
        System.out.println("Putting ES");
        if(exerciseSetDto == null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!exerciseSetRepository.existsById(exerciseSetDto.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(exerciseSetDto.getId());
        System.out.println(exerciseSetDto.getId());
        System.out.println(exerciseSetDto.getExercises().isEmpty());
        if(!exerciseSetDto.getExercises().isEmpty()){
            for (ExerciseDto exerciseDto : exerciseSetDto.getExercises()
            ) {
                for (ExerciseEntity exerciseEntity : exerciseSetEntity.getExercises()
                     ) {
                    System.out.println(exerciseSetEntity.getExercises().isEmpty());
                    System.out.println(!exerciseEntity.getId().equals(exerciseDto.getId()));
                    if(exerciseSetEntity.getExercises().isEmpty() || !exerciseEntity.getId().equals(exerciseDto.getId())) {
                        if (exerciseRepository.existsById(exerciseDto.getId())) {

                            exerciseSetEntity.getExercises().add(exerciseRepository.getOne(exerciseDto.getId()));
                        } else {
                            return new ResponseEntity<>("A/Some id is not exist.Nothing changed", HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            }
        }
        if(!exerciseSetDto.getExerciseSetLoggings().isEmpty()){
            for (ExerciseSetLogDto exerciseSetLogDto : exerciseSetDto.getExerciseSetLoggings()
            ) {
                for (ExerciseSetLogEntity exerciseSetLogEntity : exerciseSetEntity.getExerciseSetLogs()
                ) {
                    if(!exerciseSetLogEntity.getId().equals(exerciseSetLogDto.getId())) {
                        if (exerciseSetLogRepository.existsById(exerciseSetLogDto.getId())) {
                            exerciseSetEntity.getExerciseSetLogs().add(exerciseSetLogRepository.getOne(exerciseSetLogDto.getId()));
                        } else {
                            return new ResponseEntity<>("A/Some id is not exist.Nothing changed", HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            }
        }
        exerciseSetEntity.setTitle(exerciseSetDto.getTitle() == null ? exerciseSetEntity.getTitle() : exerciseSetDto.getTitle());
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteExerciseSet(@RequestParam Long id){
        if(!exerciseSetRepository.existsById(id))
            return new ResponseEntity<>("A/Some id is not exist.Nothing changed", HttpStatus.BAD_REQUEST);
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(id);
        exerciseSetEntity.removeAllExercises();
        exerciseSetEntity.removeAllExericseSetLogs();
        exerciseSetRepository.delete(exerciseSetEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
