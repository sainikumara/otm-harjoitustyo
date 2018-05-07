
package worksheetout.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoutineTest {
    Routine routine;
    List<Exercise> exercises;
    
    @Before
    public void setUp() {
        Exercise calfRaise = new Exercise("calf raise", "kg", "repetitions");
        Exercise squat = new Exercise("squat", "kg", "repetitions");
        exercises = new ArrayList<>();
        exercises.add(calfRaise);
        exercises.add(squat);
        
        routine = new Routine("Leg day");
    }
    
    @Test
    public void nameIsCorrect() {
        assertEquals(routine.getName(), "Leg day");
    }
    
    @Test
    public void setNameWorks() {
        routine.setName("Arm day");
        assertEquals(routine.getName(), "Arm day");
    }
    
    @Test
    public void getExercisesWorks() {
        routine.setExecises(exercises);
        assertEquals(routine.getExercises(), exercises);
    }
    
    @Test
    public void getExerciseNamesWorks() {
        routine.setExecises(exercises);
        
        List<String> exerciseNames = new ArrayList<>();
        for (Exercise exercise : exercises) {
            exerciseNames.add(exercise.getName());
            exerciseNames.add("");
        }
        
        assertEquals(routine.getExerciseNames(), exerciseNames);
    }
    
    @Test
    public void getEerciseParametersWorks() {
        routine.setExecises(exercises);
        
        List<String> exerciseParameters = new ArrayList<>();
             
        for (Exercise exercise : exercises) {
            exerciseParameters.add(exercise.getParameters().get(0));
            exerciseParameters.add(exercise.getParameters().get(1));
        }
        
        assertEquals(routine.getExerciseParameters(), exerciseParameters);
    }
    
    @Test
    public void addOneExerciseWorks() {
        Exercise deadlift = new Exercise("deadlift", "kg", "repetitions");
        routine.addOneExercise(deadlift);
        
        assertEquals(routine.getOneExercise("deadlift"), deadlift);
    }
    
    @Test
    public void equalWhenSameName() {
        Routine routine2 = new Routine("Leg day");
        
        assertEquals(routine, routine2);
    }
    
    @Test
    public void nonEqualWhenDifferentName() {
        Routine routine2 = new Routine("Arm day");
        
        assertFalse(routine.equals(routine2));
    }
    
    @Test
    public void nonEqualWhenDifferentType() {
        Object o = new Object();
        assertFalse(routine.equals(o));
    }
    
    @Test
    public void toStringWorks() {
        routine.setExecises(exercises);
        assertEquals(routine.toString(), "Leg day: \ncalf raise (parameters: kg, repetitions)\nsquat (parameters: kg, repetitions)\n");
    }
}
