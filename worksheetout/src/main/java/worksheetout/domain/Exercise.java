package worksheetout.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a planned individual exercise
 */

public class Exercise {

    private String name;
    private List<String> parameterNames = new ArrayList<>();
    
    public Exercise(String newName) {
        this.name = newName;
    }
    
    public Exercise(String newName, String parameter1, String parameter2) {
        this.name = newName;
        this.parameterNames.add(parameter1);
        this.parameterNames.add(parameter2);
    }
    
    public void setName(String newName) {
        this.name = newName;
    }
    
    public void setParameters(String parameter1, String parameter2) {
        this.parameterNames.add(parameter1);
        this.parameterNames.add(parameter2);
    }

    public String getName() {
        return this.name;
    }
    
    public List<String> getParameters() {
        return this.parameterNames;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Exercise)) {
            return false;
        }
        Exercise other = (Exercise) obj;
        return this.name.equals(other.name);
    }
    
    @Override
    public String toString() {
        return this.name + " (parameters: " + this.parameterNames.get(0) + ", " + this.parameterNames.get(1) + ")";
    }

}
