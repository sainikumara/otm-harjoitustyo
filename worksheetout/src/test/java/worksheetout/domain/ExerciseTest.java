
package worksheetout.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExerciseTest {
    User u1;
    Exercise exerciseNoIdNoParameters;
    Exercise exerciseIdNoParameters;
    Exercise exerciseNoIdParameters;
    Exercise exerciseIdParameters;
    
    @Before
    public void setUp() {
        u1 = new User("tester", "Terttu");
        exerciseNoIdNoParameters = new Exercise("squat", u1);
        exerciseIdNoParameters = new Exercise(1, "deadlift", u1);
        exerciseNoIdParameters = new Exercise("calf raise", u1, "kg", "repetitions");
        exerciseIdParameters = new Exercise(2, "legpress", u1, "kg", "repetitions");
    }
    
    @Test
    public void nameIsCorrect() {
        assertTrue(exerciseNoIdNoParameters.getName().equals("squat"));
        assertTrue(exerciseIdNoParameters.getName().equals("deadlift"));
        assertTrue(exerciseNoIdParameters.getName().equals("calf raise"));
        assertTrue(exerciseIdParameters.getName().equals("legpress"));
    }
    
    @Test
    public void setNameWorks() {
        exerciseNoIdNoParameters.setName("squat with weights");
        assertTrue(exerciseNoIdNoParameters.getName().equals("squat with weights"));
    }
    
    @Test
    public void idIsCorrect() {
        assertTrue(exerciseIdParameters.getId() == 2);
    }
    
    @Test
    public void setIdWorks() {
        exerciseNoIdNoParameters.setId(3);
        assertTrue(exerciseNoIdNoParameters.getId() == 3);
    }
    
    @Test
    public void parametersAreCorrect() {
        List<String> parameters = new ArrayList<>();
        parameters.add("kg");
        parameters.add("repetitions");
        assertTrue(exerciseIdParameters.getParameters().equals(parameters));
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
    public void userIsCorrect() {
        assertTrue(exerciseNoIdNoParameters.getUser().equals(u1));
    }
    
    @Test
    public void equalWhenSameName() {
        Exercise e = new Exercise("squat", u1);
        assertTrue(e.equals(exerciseNoIdNoParameters));
    }
    
    @Test
    public void nonEqualWhenDifferentUsername() {
        Exercise e = new Exercise("squat", u1);
        assertFalse(e.equals(exerciseIdNoParameters));
    }
    
    @Test
    public void nonEqualWhenDifferentType() {
        Object o = new Object();
        assertFalse(exerciseIdNoParameters.equals(o));
    }
    
    @Test
    public void toStringWorks() {
        assertTrue(exerciseIdParameters.toString().equals("legpress (parametrit: kg, repetitions)"));
    }
}
