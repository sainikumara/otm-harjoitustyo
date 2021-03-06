package worksheetout.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing an individual exercise
 */

public class DoneExercise extends Exercise {

    private Map<String, Double> parameterValues = new HashMap<>();
    
    public DoneExercise(Exercise exercise, List<Double> values) {
        super(exercise.getName(), exercise.getParameters().get(0), exercise.getParameters().get(1));
        
        for (int i = 0; i < super.getParameters().size(); i++) {
            this.parameterValues.put(super.getParameters().get(i), values.get(i));
        }
    }
    
    public void setParameterValue(String parameterName, double value) {
        this.parameterValues.put(parameterName, value);
    }
    
    public double getParameterValue(String parameterName) {
        return this.parameterValues.getOrDefault(parameterName, -1.0);
    }
    
    
    public Map<String, Double> getParameterValues() {
        return this.parameterValues;
    }

    @Override
    public String toString() {
        return  this.getName() + ": " + this.parameterValues.toString();
    }
    
}
