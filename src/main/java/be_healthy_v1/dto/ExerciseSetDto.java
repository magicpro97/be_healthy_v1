package be_healthy_v1.dto;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSetDto {
    private Long id;
    private String title;
    List<ExerciseDto> exercises;
    List<ExerciseSetLogDto> exerciseSetLoggings;

    protected ExerciseSetDto(){
        exercises = new ArrayList<>();
        exerciseSetLoggings = new ArrayList<>();
    }

    public ExerciseSetDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ExerciseDto> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDto> exercises) {
        this.exercises = exercises;
    }

    public List<ExerciseSetLogDto> getExerciseSetLoggings() {
        return exerciseSetLoggings;
    }

    public void setExerciseSetLoggings(List<ExerciseSetLogDto> exerciseSetLoggings) {
        this.exerciseSetLoggings = exerciseSetLoggings;
    }

    public void addExerciseDto(ExerciseDto exerciseDto){
        exerciseDto.getExerciseSets().add(this);
        exercises.add(exerciseDto);
    }

    public void removeExerciseDto(ExerciseDto exerciseDto){
        exerciseDto.getExerciseSets().remove(this);
        exercises.remove(exerciseDto);
    }

    public  void addExerciseSetLoggingDto(ExerciseSetLogDto exerciseSetLogDto){
        exerciseSetLogDto.setExerciseSet(this);
        exerciseSetLoggings.add(exerciseSetLogDto);
    }
    public  void removeExerciseSetLoggingDto(ExerciseSetLogDto exerciseSetLogDto){
        exerciseSetLogDto.setExerciseSet(null);
        exerciseSetLoggings.remove(exerciseSetLogDto);
    }

    @Override
    public String toString() {
        return "ExerciseSetDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", exercises=" + exercises +
                ", exerciseSetLoggings=" + exerciseSetLoggings +
                '}';
    }
}
