package worksheetout.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing an individual workout session that follows a workout routine
 */

public class WorkoutSession {

    private int id;
    private Date date;
    private Routine routine;
    private List<DoneExercise> sessionContents;

    public WorkoutSession(int id, Date newDate, Routine newRoutine) {
        this.id = id;
        this.date = newDate;
        this.routine = newRoutine;
        this.sessionContents = new ArrayList<>();
    }
    
    public WorkoutSession(Date newDate, Routine newRoutine) {
        this.date = newDate;
        this.routine = newRoutine;
        this.sessionContents = new ArrayList<>();
    }
    
    public Routine getRoutine() {
        return this.routine;
    }
    
    public void setRoutine(Routine newRoutine) {
        this.routine = newRoutine;
    }
    
    public List<DoneExercise> getSessionContents() {
        return this.sessionContents;
    }
    
    public void setSessionContents(List<DoneExercise> newContents) {
        this.sessionContents = newContents;
    }
    
    public void addOneDoneExercise(String name, List<Integer> parameterValues) {
        Exercise exercise = this.routine.getOneExercise(name);
        DoneExercise doneExercise = new DoneExercise(exercise, parameterValues);
        this.sessionContents.add(doneExercise);
    }
    
    public void setId(int newId) {
        this.id = newId;
    }
    
    public void setDate(Date newDate) {
        this.date = newDate;
    }

    public int getId() {
        return this.id;
    }
    
    public Date getDate() {
        return this.date;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorkoutSession)) {
            return false;
        }
        WorkoutSession other = (WorkoutSession) obj;
        return id == other.id;
    }
    
    @Override
    public String toString() {
        String sessionAsAString = this.date.toString() + ": \n";
        
        for (DoneExercise exercise : this.sessionContents) {
            sessionAsAString += exercise.toString() + "\n";
        }
        
        return sessionAsAString;
    }

}
