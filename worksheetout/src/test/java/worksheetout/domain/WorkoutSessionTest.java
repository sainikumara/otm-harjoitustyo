
package worksheetout.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkoutSessionTest {
    WorkoutSession session;
    Routine routine;
    User user;
    List<Exercise> exercises;
    List<DoneExercise> doneExercises;
    Date date;
    DateFormat dateFormat;
    
    @Before
    public void setUp() {
        Exercise calfRaise = new Exercise("calf raise", "kg", "repetitions");
        Exercise squat = new Exercise("squat", "kg", "repetitions");
        exercises = new ArrayList<>();
        exercises.add(calfRaise);
        exercises.add(squat);
        user = new User("tester", "Terttu");
        routine = new Routine("Leg day", user);
        routine.setExecises(exercises);
        
        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse("2018-05-02");
        } catch (Exception e) {
        }
        session = new WorkoutSession(date, routine);
        
        List<Integer> valuesCalf = new ArrayList<>();
        valuesCalf.add(70);
        valuesCalf.add(30);
        DoneExercise doneCalfRaise = new DoneExercise(calfRaise, valuesCalf);
        
        List<Integer> valuesSquat = new ArrayList<>();
        valuesSquat.add(40);
        valuesSquat.add(20);
        DoneExercise doneSquat = new DoneExercise(squat, valuesSquat);
        
        doneExercises = new ArrayList<>();
        doneExercises.add(doneCalfRaise);
        doneExercises.add(doneSquat);
        
    }
    
    @Test
    public void dateIsCorrect() {
        assertEquals(session.getDate(), date);
    }
    
    @Test
    public void setDateWorks() {
        Date date2;
        try {
            date2 = dateFormat.parse("2018-05-01");
            session.setDate(date2);
            assertEquals(session.getDate(), date2);
        } catch (Exception e) {
        }
    }
    
    @Test
    public void getRoutineWorks() {
        assertEquals(session.getRoutine(), routine);
    }
    
    @Test
    public void setRoutineWorks() {
        Routine routine2 = new Routine("Arm day", user);
        session.setRoutine(routine2);
        
        assertEquals(session.getRoutine(), routine2);
    }
    
    @Test
    public void setSessionContentsWorks() {
        session.setSessionContents(doneExercises);
        
        assertEquals(session.getSessionContents(), doneExercises);
    }
    
    @Test
    public void getExercisesWorks() {
        routine.setExecises(exercises);
        assertEquals(routine.getExercises(), exercises);
    }
    
    @Test
    public void getDateAndExerciseParameterValuesWorks() {
        session.setSessionContents(doneExercises);
        
        List<String> values = new ArrayList<>();
        values.add("2018-05-02");
        values.add("70");
        values.add("30");
        values.add("40");
        values.add("20");
        
        assertEquals(session.getDateAndExerciseParameterValues(), values);
    }
    
    @Test
    public void addOneDoneExerciseWorks() {
        List<Integer> valuesSquat = new ArrayList<>();
        valuesSquat.add(88);
        valuesSquat.add(10);
        session.addOneDoneExercise("squat", valuesSquat);
        
        Exercise squat = new Exercise("squat", "kg", "repetitions");
        DoneExercise doneSquat = new DoneExercise(squat, valuesSquat);
        List<DoneExercise> squatlist = new ArrayList<>();
        squatlist.add(doneSquat);
        
        assertEquals(session.getSessionContents(), squatlist);
    }
    
    @Test
    public void equalWhenSameDateAndRoutine() {
        Routine routine2 = new Routine("Leg day", user);
        WorkoutSession session2 = new WorkoutSession(date, routine2);
        
        assertEquals(session, session2);
    }
    
    @Test
    public void nonEqualWhenDifferentRoutine() {
        Routine routine2 = new Routine("Arm day", user);
        WorkoutSession session2 = new WorkoutSession(date, routine2);
        
        assertFalse(session.equals(session2));
    }
    
    @Test
    public void nonEqualWhenDifferentDate() {
        Date date2;
        try {
            date2 = dateFormat.parse("2018-05-01");
            WorkoutSession session2 = new WorkoutSession(date2, routine);
            assertFalse(session.equals(session2));
        } catch (Exception e) {
        }
    }
    
    @Test
    public void nonEqualWhenDifferentType() {
        Object o = new Object();
        assertFalse(session.equals(o));
    }
    
    @Test
    public void toStringWorks() {
        session.setSessionContents(doneExercises);
        assertEquals(session.toString(), "Wed May 02 00:00:00 EEST 2018: \ncalf raise: {kg=70, repetitions=30}\nsquat: {kg=40, repetitions=20}\n");
    }
}
