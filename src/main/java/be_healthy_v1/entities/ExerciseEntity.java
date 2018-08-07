package be_healthy_v1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise")
public class ExerciseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToMany(mappedBy = "exercises")
    @JsonBackReference
    private List<ExerciseSetEntity> exerciseSets;

    protected ExerciseEntity() {
        super();
        exerciseSets = new ArrayList<>();
    }

    public ExerciseEntity(String title, String description, List<ExerciseSetEntity> exerciseSets) {
        this.title = title;
        this.description = description;
        this.exerciseSets = exerciseSets;
    }

    public ExerciseEntity(String title, String description) {
        this.title = title;
        this.description = description;
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

    public List<ExerciseSetEntity> getExerciseSets() {
        return exerciseSets;
    }

    public void setExerciseSets(List<ExerciseSetEntity> exerciseSets) {
        this.exerciseSets = exerciseSets;
    }

    public void addExerciseSet(ExerciseSetEntity exerciseSetEntity){
        exerciseSetEntity.getExercises().add(this);
        exerciseSets.add(exerciseSetEntity);
    }

    public  void removeExerciseSet(ExerciseSetEntity exerciseSetEntity){
        exerciseSetEntity.getExercises().remove(this);
        exerciseSets.remove(exerciseSets);
    }

    @Override
    public String toString() {
        return "ExerciseEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", exerciseSets=" + exerciseSets +
                '}';
    }
}
