package be_healthy_v1.controllers;

import be_healthy_v1.dto.ExerciseSetLogDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exerciseSetLogs")
public class ExerciseSetLogController {
    public static final Logger logger = LoggerFactory.getLogger(ExerciseSetLogController.class);

    private ExerciseRepository exerciseRepository;
    private ExerciseSetRepository exerciseSetRepository;
    private ExerciseSetLogRepository exerciseSetLogRepository;

    @Autowired
    public ExerciseSetLogController(ExerciseRepository exerciseRepository,
                                    ExerciseSetRepository exerciseSetRepository,
                                    ExerciseSetLogRepository exerciseSetLogRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseSetRepository = exerciseSetRepository;
        this.exerciseSetLogRepository = exerciseSetLogRepository;
    }

    @GetMapping
    public ResponseEntity getExerciseSetLogs(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        PageRequest pageRequest = new PageRequest(page -1,size);
        Page<ExerciseSetLogEntity> pageESL = exerciseSetLogRepository.findAll(pageRequest);
        return new ResponseEntity<>(pageESL, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getExerciseSetLog(@PathVariable("id") Long id){
        logger.info("Retrieve a exercise set log by id {}", id);
        if(exerciseSetLogRepository.existsById(id)){
            ExerciseSetLogEntity exerciseSetLogEntity = exerciseSetLogRepository.getOne(id);
            return new ResponseEntity<>(exerciseSetLogEntity,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/{id}/exerciseSet")
    public ResponseEntity getExerciseSetByESId(@PathVariable("id") Long id){
        logger.info("Retrieve a exercise set by id {}", id);
        if(exerciseSetLogRepository.existsById(id)){
            ExerciseSetLogEntity exerciseSetLogEntity = exerciseSetLogRepository.getOne(id);
            return new ResponseEntity<>(exerciseSetLogEntity.getExerciseSet(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity addExerciseSetLog(@RequestBody ExerciseSetLogDto exerciseSetLogDto){
        if(exerciseSetLogDto == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(exerciseSetLogDto.getExerciseSet().getId());
        exerciseSetLogRepository.save(new ExerciseSetLogEntity(exerciseSetLogDto.getDateLog(),exerciseSetLogDto.getRating()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/exerciseSet/{esId}")
    public ResponseEntity addExericeSetAssociate(@PathVariable("id") Long id, @PathVariable("id") Long esId){
        if(id == null || esId == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!exerciseSetLogRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!exerciseSetRepository.existsById(esId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(esId);
        ExerciseSetLogEntity exerciseSetLogEntity = exerciseSetLogRepository.getOne(id);
        exerciseSetEntity.getExerciseSetLogs().add(exerciseSetLogEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteExerciseSetLog(@PathVariable("id") Long id){
        if(id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(exerciseSetLogRepository.existsById(id)){
            exerciseSetLogRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
