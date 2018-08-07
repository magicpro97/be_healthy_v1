package be_healthy_v1.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exercise_set_log")
public class ExerciseSetLogEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ExerciseSetEntity exerciseSet;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateLog;
    private int rating;

    public ExerciseSetLogEntity() {
        super();
    }

    public ExerciseSetLogEntity(Date dateLog, int rating) {
        this.dateLog = dateLog;
        this.rating = rating;
    }

    public ExerciseSetLogEntity(ExerciseSetEntity exerciseSet, Date dateLog, int rating) {
        addExerciseSet(exerciseSet);
        this.dateLog = dateLog;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExerciseSetEntity getExerciseSet() {
        return exerciseSet;
    }

    public void setExerciseSet(ExerciseSetEntity exerciseSet) {
        this.exerciseSet = exerciseSet;
    }

    public Date getDateLog() {
        return dateLog;
    }

    public void setDateLog(Date dateLog) {
        this.dateLog = dateLog;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void addExerciseSet(ExerciseSetEntity exerciseSetEntity){
        exerciseSetEntity.getExerciseSetLogs().add(this);
        setExerciseSet(exerciseSetEntity);
    }

    public void removeExerciseSet(ExerciseSetEntity exerciseSetEntity){
        exerciseSetEntity.getExerciseSetLogs().remove(this);
        setExerciseSet(null);
    }

    @Override
    public String toString() {
        return "ExerciseSetLogEntity{" +
                "id=" + id +
                ", exerciseSet=" + exerciseSet +
                ", dateLog=" + dateLog +
                ", rating=" + rating +
                '}';
    }
}
