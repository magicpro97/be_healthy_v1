package be_healthy_v1.controllers;

import be_healthy_v1.dto.ExerciseDto;
import be_healthy_v1.dto.ExerciseSetDto;
import be_healthy_v1.entities.ExerciseEntity;
import be_healthy_v1.entities.ExerciseSetEntity;
import be_healthy_v1.respotories.ExerciseRepository;
import be_healthy_v1.respotories.ExerciseSetLogRepository;
import be_healthy_v1.respotories.ExerciseSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/exercises")
public class ExerciseController {
    public static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    private ExerciseRepository exerciseRepository;
    private ExerciseSetRepository exerciseSetRepository;
    private ExerciseSetLogRepository exerciseSetLogRepository;

    @Autowired
    public ExerciseController(ExerciseRepository exerciseRepository,
                              ExerciseSetRepository exerciseSetRepository,
                              ExerciseSetLogRepository exerciseSetLogRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseSetRepository = exerciseSetRepository;
        this.exerciseSetLogRepository = exerciseSetLogRepository;
    }

    @GetMapping
    public ResponseEntity getExercises(@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize){
        logger.info("Retrieve all exercises");
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize);
        Page<ExerciseEntity> page = exerciseRepository.findAll(pageRequest);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getExercise1(@PathVariable("id") Long id){
        logger.info("Fetching exercise with id {}",id);
        if(exerciseRepository.existsById(id)) {
            ExerciseEntity exerciseEntity = exerciseRepository.getOne(id);
            return new ResponseEntity(exerciseEntity,HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity addExercise(@RequestBody ExerciseDto exerciseDto, UriComponentsBuilder ucBuilder){
        logger.info("Creating exercise : {}", exerciseDto);
        ExerciseEntity exerciseEntity = null;
        if(exerciseDto == null) {
            return new ResponseEntity<>("Null request", HttpStatus.BAD_REQUEST);
        }
        if(exerciseDto.getExerciseSets() == null || exerciseDto.getExerciseSets().isEmpty()) {
            exerciseEntity = new ExerciseEntity(exerciseDto.getTitle(),exerciseDto.getDescription());
            exerciseRepository.save(exerciseEntity);
        }else {
            List<ExerciseSetEntity> exerciseSetEntities = new ArrayList<>();
            boolean err = false;
            String errDetail = "";
            for (ExerciseSetDto exerciseSetDto : exerciseDto.getExerciseSets()
            ) {
                if (!exerciseSetRepository.existsById(exerciseSetDto.getId())) {
                    err = true;
                    errDetail += exerciseSetDto.getId() +" ";
                } else {
                    exerciseSetEntities.add(exerciseSetRepository.getOne(exerciseDto.getId()));
                }
            }
            if(err){
                return new ResponseEntity<>( errDetail+ "ís not found. Nothing added.",
                        HttpStatus.NOT_FOUND);
            }
            exerciseEntity = new ExerciseEntity(exerciseDto.getTitle(), exerciseDto.getDescription(), exerciseSetEntities);
            exerciseRepository.save(exerciseEntity);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/exercises/{id}").buildAndExpand(exerciseEntity.getId()).toUri());
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }

   @PutMapping(value = "/{id}")
    public ResponseEntity updateExercise(@PathVariable("id")Long id, @RequestBody ExerciseDto exerciseDto){
        logger.info("Updating exercise by id {}", id);
       logger.info("Updating exercise {}", exerciseDto);
       if(exerciseDto == null) {
           return new ResponseEntity<>("Null object.", HttpStatus.BAD_REQUEST);
       }
       if(!exerciseSetRepository.existsById(id)){
           return new ResponseEntity<>("Exercise is not found.", HttpStatus.NOT_FOUND);
       }
       ExerciseEntity exerciseEntity = exerciseRepository.getOne(id);
       exerciseEntity.setTitle(exerciseDto.getTitle() == null ? exerciseEntity.getTitle() : exerciseDto.getTitle());
       exerciseEntity.setDescription(exerciseDto.getDescription() == null ? exerciseEntity.getDescription() : exerciseDto.getDescription());

       exerciseRepository.save(exerciseEntity);
       return new ResponseEntity(exerciseEntity,HttpStatus.OK);
   }

    @PostMapping(value = "/{id}/exerciseSet/{esId}")
    public ResponseEntity updateExercise(@PathVariable("id")Long id, @PathVariable("esId") Long esId){
        logger.info("Updating exercise by id {}", id);
        logger.info("Updating exercise add exercise set associate by id {}", esId);
        if(esId == null || id == null) {
            return new ResponseEntity<>("Null object.", HttpStatus.BAD_REQUEST);
        }
        if(!exerciseRepository.existsById(id)){
            return new ResponseEntity<>("Exercise is not found.", HttpStatus.NOT_FOUND);
        }
        if(!exerciseSetRepository.existsById(esId)){
            return new ResponseEntity<>("Exercise is not found.", HttpStatus.NOT_FOUND);
        }
        ExerciseEntity exerciseEntity = exerciseRepository.getOne(id);
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(esId);
        for (ExerciseEntity setEntity :exerciseSetEntity.getExercises()
             ) {
            if(setEntity.getId().equals(id)){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        exerciseSetEntity.getExercises().add(exerciseEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity(exerciseSetEntity,HttpStatus.OK);
    }

   @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteExercise(@PathVariable("id") Long id){
        logger.info("Deleting exercise by id {}", id);
       if(id == null){
           return new ResponseEntity<>("Id is not found.",HttpStatus.NOT_FOUND);
       }
       if(!exerciseRepository.existsById(id)){
           return new ResponseEntity<>("Exercise is not found.", HttpStatus.NOT_FOUND);
       }
       ExerciseEntity exerciseEntity = exerciseRepository.getOne(id);
       for(ExerciseSetEntity exerciseSetEntity: exerciseEntity.getExerciseSets()){
           exerciseSetEntity.getExercises().remove(exerciseEntity);
       }
       exerciseRepository.delete(exerciseEntity);
       return new ResponseEntity<>(HttpStatus.OK);
   }

}
