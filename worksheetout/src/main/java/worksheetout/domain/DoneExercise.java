package worksheetout.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing an individual exercise
 */

public class DoneExercise extends Exercise {

    private Map<String, Integer> parameterValues = new HashMap<>();
    
    public DoneExercise(int newId, String newName, User newUser, List<String> parameterNames, List<Integer> values) {
        super(newId, newName, newUser);
        
        for (int i = 0; i < parameterNames.size(); i++) {
            this.parameterValues.put(parameterNames.get(i), values.get(i));
        }
    }
    
    public DoneExercise(String newName, User newUser, List<String> parameterNames, List<Integer> values) {
        super(newName, newUser);
        
        for (int i = 0; i < parameterNames.size(); i++) {
            this.parameterValues.put(parameterNames.get(i), values.get(i));
        }
    }
    
    public void setParameterValue(String parameterName, int value) {
        this.parameterValues.put(parameterName, value);
    }
    
    public int getParameterValue(String parameterName) {
        return this.parameterValues.getOrDefault(parameterName, -1);
     }
    
    
    public Map<String, Integer> getParameterValues() {
        return this.parameterValues;
    }

    @Override
    public String toString() {
        return  this.getName() + ": " + this.parameterValues.toString();
    }
    
}
