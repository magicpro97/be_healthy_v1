package be_healthy_v1.dto;

import java.util.Date;

public class ExerciseSetLogDto {
    private Long id;
    private Date dateLog;
    private int rating;
    private ExerciseSetDto exerciseSet;

    public ExerciseSetLogDto(Date dateLog, int rating) {
        this.dateLog = dateLog;
        this.rating = rating;
    }

    public ExerciseSetLogDto(Date dateLog, int rating, ExerciseSetDto exerciseSet) {
        this.dateLog = dateLog;
        this.rating = rating;
        this.exerciseSet = exerciseSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ExerciseSetDto getExerciseSet() {
        return exerciseSet;
    }

    public void setExerciseSet(ExerciseSetDto exerciseSet) {
        this.exerciseSet = exerciseSet;
    }

    @Override
    public String toString() {
        return "ExerciseSetLogDto{" +
                "id=" + id +
                ", dateLog=" + dateLog +
                ", rating=" + rating +
                ", exerciseSet=" + exerciseSet +
                '}';
    }
}
