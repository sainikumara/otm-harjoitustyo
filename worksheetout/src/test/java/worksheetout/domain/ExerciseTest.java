
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
    Exercise excerciseNoIdNoParameters;
    Exercise excerciseIdNoParameters;
    Exercise excerciseNoIdParameters;
    Exercise excerciseIdParameters;
    
    @Before
    public void setUp() {
        u1 = new User("tester", "Terttu");
        excerciseNoIdNoParameters = new Exercise("squat", u1);
        excerciseIdNoParameters = new Exercise(1, "deadlift", u1);
        excerciseNoIdParameters = new Exercise("calf raise", u1, "kg", "repetitions");
        excerciseIdParameters = new Exercise(2, "legpress", u1, "kg", "repetitions");
    }
    
    @Test
    public void nameIsCorrect() {
        assertTrue(excerciseNoIdNoParameters.getName().equals("squat"));
        assertTrue(excerciseIdNoParameters.getName().equals("deadlift"));
        assertTrue(excerciseNoIdParameters.getName().equals("calf raise"));
        assertTrue(excerciseIdParameters.getName().equals("legpress"));
    }
    
    @Test
    public void setNameWorks() {
        excerciseNoIdNoParameters.setName("squat with weights");
        assertTrue(excerciseNoIdNoParameters.getName().equals("squat with weights"));
    }
    
    @Test
    public void idIsCorrect() {
        assertTrue(excerciseIdParameters.getId() == 2);
    }
    
    @Test
    public void setIdWorks() {
        excerciseNoIdNoParameters.setId(3);
        assertTrue(excerciseNoIdNoParameters.getId() == 3);
    }
    
    @Test
    public void parametersAreCorrect() {
        List<String> parameters = new ArrayList<>();
        parameters.add("kg");
        parameters.add("repetitions");
        assertTrue(excerciseIdParameters.getParameters().equals(parameters));
    }
    
    @Test
    public void setParametersWorks() {
        excerciseNoIdNoParameters.setParameters("kg", "repetitions");
        List<String> parameters = new ArrayList<>();
        parameters.add("kg");
        parameters.add("repetitions");
        assertTrue(excerciseNoIdNoParameters.getParameters().equals(parameters));
    }
    
    @Test
    public void userIsCorrect() {
        assertTrue(excerciseNoIdNoParameters.getUser().equals(u1));
    }
    
    @Test
    public void equalWhenSameName() {
        Exercise e = new Exercise("squat", u1);
        assertTrue(e.equals(excerciseNoIdNoParameters));
    }
    
    @Test
    public void nonEqualWhenDifferentUsername() {
        Exercise e = new Exercise("squat", u1);
        assertFalse(e.equals(excerciseIdNoParameters));
    }
    
    @Test
    public void nonEqualWhenDifferentType() {
        Object o = new Object();
        assertFalse(excerciseIdNoParameters.equals(o));
    }
    
    @Test
    public void toStringWorks() {
        assertTrue(excerciseIdParameters.toString().equals("legpress (parametrit: kg, repetitions)"));
    }
}
