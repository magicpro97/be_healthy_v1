package be_healthy_v1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise_set")
public class ExerciseSetEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "exercise_set_id")
    private List<ExerciseSetLogEntity> exerciseSetLogs;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="e_es",
    joinColumns = @JoinColumn(name ="es_id"),
    inverseJoinColumns = @JoinColumn(name = "e_id"))
    @JsonManagedReference
    private List<ExerciseEntity> exercises;

    public ExerciseSetEntity(String title) {
        this.title = title;
        exercises = new ArrayList<>();
        exerciseSetLogs = new ArrayList<>();
    }

    protected ExerciseSetEntity() {
        super();
        exercises = new ArrayList<>();
        exerciseSetLogs = new ArrayList<>();
    }

    public ExerciseSetEntity(String title, List<ExerciseSetLogEntity> exerciseSetLogs, List<ExerciseEntity> exercises) {
        this.title = title;
        this.exerciseSetLogs = exerciseSetLogs;
        this.exercises = exercises;
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

    public List<ExerciseSetLogEntity> getExerciseSetLogs() {
        return exerciseSetLogs;
    }

    public void setExerciseSetLogs(List<ExerciseSetLogEntity> exerciseSetLogs) {
        this.exerciseSetLogs = exerciseSetLogs;
    }

    public List<ExerciseEntity> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseEntity> exercises) {
        this.exercises = exercises;
    }

    public void addExerciseEntity(ExerciseEntity exerciseEntity){
        exerciseEntity.getExerciseSets().add(this);
        exercises.add(exerciseEntity);
    }

    public void removeExerciseEntity(ExerciseEntity exerciseEntity){
        exerciseEntity.getExerciseSets().remove(this);
        exercises.remove(exerciseEntity);
    }

    public  void addExerciseSetLoggingEntity(ExerciseSetLogEntity exerciseSetLogEntity){
        exerciseSetLogEntity.setExerciseSet(this);
        exerciseSetLogs.add(exerciseSetLogEntity);
    }
    public  void removeExerciseSetLoggingEntity(ExerciseSetLogEntity exerciseSetLogEntity){
        exerciseSetLogEntity.setExerciseSet(null);
        exerciseSetLogs.remove(exerciseSetLogEntity);
    }
    public void removeAllExericseSetLogs(){
        exerciseSetLogs.clear();
    }

    public  void removeAllExercises(){
        exercises.clear();
    }

    @Override
    public String toString() {
        return "ExerciseSetEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", exerciseSetLogs=" + exerciseSetLogs +
                ", exercises=" + exercises +
                '}';
    }
}
