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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


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
        logger.info("Retrieve all exercise sets ");
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
    public ResponseEntity addExerciseSet(@RequestBody ExerciseSetDto exerciseSetDto,  UriComponentsBuilder ucBuilder){
        logger.info("Add a new exercise set {}", exerciseSetDto);
        if(exerciseSetDto == null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ExerciseSetEntity exerciseSetEntity = new ExerciseSetEntity(exerciseSetDto.getTitle());
        exerciseSetRepository.save(exerciseSetEntity);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/exerciseSets/{id}").buildAndExpand(exerciseSetEntity.getId()).toUri());
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/exercise/{eId}")
    public ResponseEntity addExerciseAssociate(@PathVariable("id") Long id, @PathVariable("eId") Long eId){
        logger.info("Add Exercise Associate {} | {}",id,eId);
        if(id == null || eId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!exerciseSetRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!exerciseRepository.existsById(eId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(id);
        ExerciseEntity exerciseEntity = exerciseRepository.getOne(eId);
        for (ExerciseEntity setEntity: exerciseSetEntity.getExercises()
             ) {
           if(setEntity.getId().equals(id))
               return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        exerciseSetEntity.getExercises().add(exerciseEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity<>(exerciseSetEntity,HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/exerciseSetLog/{eslId}")
    public ResponseEntity addExerciseSetLogAssociate(@PathVariable("id") Long id, @PathVariable("eslId") Long eslId){
        logger.info("Add Exercise Set Log Associate {} | {}",id,eslId);
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
        if(exerciseSetEntity.getExerciseSetLogs().contains(exerciseSetLogEntity)){
            return  new ResponseEntity<>(exerciseSetLogEntity,HttpStatus.CONFLICT);
        }
        exerciseSetEntity.getExerciseSetLogs().add(exerciseSetLogEntity);
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity<>(exerciseSetEntity,HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateExerciseSet(@PathVariable Long id, @RequestBody ExerciseSetDto exerciseSetDto){
        logger.info("Update Exercise Set by id {}", id);
        logger.info("{}",exerciseSetDto);
        if(exerciseSetDto == null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!exerciseSetRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(id);
        exerciseSetEntity.setTitle(exerciseSetDto.getTitle() == null ? exerciseSetEntity.getTitle() : exerciseSetDto.getTitle());
        exerciseSetRepository.save(exerciseSetEntity);
        return new ResponseEntity(exerciseSetEntity,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteExerciseSet(@PathVariable("id") Long id){
        logger.info("Delete Exercise Set by id {}", id);
        if(!exerciseSetRepository.existsById(id))
            return new ResponseEntity<>("A/Some id is not exist.Nothing changed", HttpStatus.NOT_FOUND);
        ExerciseSetEntity exerciseSetEntity = exerciseSetRepository.getOne(id);
        exerciseSetEntity.removeAllExercises();
        exerciseSetEntity.removeAllExericseSetLogs();
        exerciseSetRepository.delete(exerciseSetEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
