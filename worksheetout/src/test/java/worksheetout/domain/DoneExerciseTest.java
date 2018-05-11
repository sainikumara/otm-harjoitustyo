
package worksheetout.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DoneExerciseTest {
    DoneExercise doneExercise;
    Exercise exercise;
    List<Double> values;
    
    @Before
    public void setUp() {
        exercise = new Exercise("calf raise", "kg", "repetitions");
        values = new ArrayList<>();
        values.add(70.0);
        values.add(30.0);
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
        Map<String, Double> testParameterValues = new HashMap<>();
        testParameterValues.put("kg", 70.0);
        testParameterValues.put("repetitions", 30.0);
        
        assertEquals(doneExercise.getParameterValues(), testParameterValues);
    }
    
    @Test
    public void getParameterValueWorks() {
        assertTrue(70.0 == doneExercise.getParameterValue("kg"));
    }
    
    @Test
    public void setParameterValueWorks() {
        doneExercise.setParameterValue("repetitions", 40.0);
        assertTrue(40.0 == doneExercise.getParameterValue("repetitions"));
    }
    
    @Test
    public void toStringWorks() {
        assertEquals(doneExercise.toString(), "calf raise: {kg=70.0, repetitions=30.0}");
    }
}
