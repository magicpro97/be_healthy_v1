package be_healthy_v1.dto;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDto {
    private Long id;
    private String title;
    private String description;
    private List<ExerciseSetDto> exerciseSets;

    protected  ExerciseDto(){
        exerciseSets = new ArrayList<>();
    }

    public ExerciseDto(Long id, String title, String description, List<ExerciseSetDto> exerciseSets) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.exerciseSets = exerciseSets;
    }

    public ExerciseDto(String title, String description, List<ExerciseSetDto> exerciseSets) {
        this.title = title;
        this.description = description;
        this.exerciseSets = exerciseSets;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ExerciseSetDto> getExerciseSets() {
        return exerciseSets;
    }

    public void setExerciseSets(List<ExerciseSetDto> exerciseSets) {
        this.exerciseSets = exerciseSets;
    }

    @Override
    public String toString() {
        return "ExerciseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", exerciseSets=" + exerciseSets +
                '}';
    }
}
