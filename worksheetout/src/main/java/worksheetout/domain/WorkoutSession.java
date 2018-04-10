package worksheetout.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing an individual workout session that follows a workout routine
 */

public class WorkoutSession {

    private int id;
    private User user;
    private Date date;
    private Routine routine;
    private Map<String, DoneExercise> sessionContents;

    public WorkoutSession(int id, User newUser, Date newDate, Routine newRoutine) {
        this.id = id;
        this.user = newUser;
        this.date = newDate;
        this.routine = newRoutine;
        this.sessionContents = new HashMap<>();
    }
    
    public WorkoutSession(User newUser, Date newDate, Routine newRoutine) {;
        this.user = newUser;
        this.date = newDate;
        this.routine = newRoutine;
        this.sessionContents = new HashMap<>();
    }
    
    public void setId(int newId) {
        this.id = newId;
    }
    
    public void setDate(Date newDate) {
        this.date = newDate;
    }

    public User getUser() {
        return this.user;
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

}
