
package worksheetout.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExerciseTest {
    Exercise exerciseNoIdNoParameters;
    Exercise exerciseNoIdParameters;
    
    @Before
    public void setUp() {
        exerciseNoIdNoParameters = new Exercise("squat");
        exerciseNoIdParameters = new Exercise("calf raise", "kg", "repetitions");
    }
    
    @Test
    public void nameIsCorrect() {
        assertTrue(exerciseNoIdNoParameters.getName().equals("squat"));
        assertTrue(exerciseNoIdParameters.getName().equals("calf raise"));
    }
    
    @Test
    public void setNameWorks() {
        exerciseNoIdNoParameters.setName("squat with weights");
        assertTrue(exerciseNoIdNoParameters.getName().equals("squat with weights"));
    }
    
    @Test
    public void parametersAreCorrect() {
        List<String> parameters = new ArrayList<>();
        parameters.add("kg");
        parameters.add("repetitions");
        assertTrue(exerciseNoIdParameters.getParameters().equals(parameters));
    }
    
    @Test
    public void setParametersWorks() {
        exerciseNoIdNoParameters.setParameters("kg", "repetitions");
        List<String> parameters = new ArrayList<>();
        parameters.add("kg");
        parameters.add("repetitions");
        assertTrue(exerciseNoIdNoParameters.getParameters().equals(parameters));
    }
    
    @Test
    public void equalWhenSameName() {
        Exercise e = new Exercise("squat");
        assertTrue(e.equals(exerciseNoIdNoParameters));
    }
    
    @Test
    public void nonEqualWhenDifferentName() {
        Exercise e = new Exercise("squat");
        assertFalse(e.equals(exerciseNoIdParameters));
    }
    
    @Test
    public void nonEqualWhenDifferentType() {
        Object o = new Object();
        assertFalse(exerciseNoIdParameters.equals(o));
    }
    
    @Test
    public void toStringWorks() {
        assertTrue(exerciseNoIdParameters.toString().equals("calf raise (parameters: kg, repetitions)"));
    }
}
