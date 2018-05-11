package worksheetout.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing an individual workout session that follows a workout routine
 */

public class WorkoutSession {

    private Date date;
    private Routine routine;
    private List<DoneExercise> sessionContents;
    
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
    
    /**
     * 
     * @return the date of the workout session and all the parameter values
     */
    
    public List<String> getDateAndExerciseParameterValues() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sessionDate = dateFormat.format(this.date);
        
        List<String> values = new ArrayList<>();
        values.add(sessionDate);
        
        for (DoneExercise exercise : this.sessionContents) {
            values.add(Double.toString(exercise.getParameterValue(exercise.getParameters().get(0))));
            values.add(Double.toString(exercise.getParameterValue(exercise.getParameters().get(1))));
        }
        
        return values;
    }
    
    /**
     * Adding one exercise to the session
     * @param name  name of the exercise to be added
     * @param parameterValues   values of the parameters of the exercise
     */
    
    public void addOneDoneExercise(String name, List<Double> parameterValues) {
        Exercise exercise = this.routine.getOneExercise(name);
        DoneExercise doneExercise = new DoneExercise(exercise, parameterValues);
        this.sessionContents.add(doneExercise);
    }
    
    public void setDate(Date newDate) {
        this.date = newDate;
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
        return (this.date.equals(other.date)) && (this.routine.equals(other.routine));
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
