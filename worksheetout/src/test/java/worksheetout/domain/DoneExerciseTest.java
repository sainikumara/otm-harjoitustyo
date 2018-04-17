
package worksheetout.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DoneExerciseTest {
    User u1;
    DoneExercise doneExercise;
    Exercise exercise;
    List<Integer> values;
    
    @Before
    public void setUp() {
        u1 = new User("tester", "Terttu");
        exercise = new Exercise("calf raise", u1, "kg", "repetitions");
        values = new ArrayList<>();
        values.add(70);
        values.add(30);
        doneExercise = new DoneExercise(exercise, values);
    }
    
    @Test
    public void nameIsCorrect() {
        assertTrue(doneExercise.getName().equals("calf raise"));
    }
    
    @Test
    public void setNameWorks() {
        doneExercise.setName("squat with weights");
        assertTrue(doneExercise.getName().equals("squat with weights"));
    }
    
    @Test
    public void parameterValuesCorrect() {
        Map<String, Integer> testParameterValues = new HashMap<>();
        testParameterValues.put("kg", 70);
        testParameterValues.put("repetitions", 30);
        
        assertEquals(doneExercise.getParameterValues(), testParameterValues);
    }
    
    @Test
    public void getParameterValueWorks() {
        assertEquals(doneExercise.getParameterValue("kg"), 70);
    }
    
    @Test
    public void setParameterValueWorks() {
        doneExercise.setParameterValue("repetitions", 40);
        assertEquals(doneExercise.getParameterValue("repetitions"), 40);
    }
    
    @Test
    public void toStringWorks() {
        assertEquals(doneExercise.toString(), "calf raise: {kg=70, repetitions=30}");
    }
}
