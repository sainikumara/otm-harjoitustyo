package worksheetout.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a planned individual exercise
 */

public class Exercise {

    private int id;
    private String name;
    private List<String> parameterNames = new ArrayList<>();

    public Exercise(int id, String newName) {
        this.id = id;
        this.name = newName;
    }
    
    public Exercise(String newName) {
        this.name = newName;
    }
    
    public Exercise(int id, String newName, String parameter1, String parameter2) {
        this.id = id;
        this.name = newName;
        this.parameterNames.add(parameter1);
        this.parameterNames.add(parameter2);
    }
    
    public Exercise(String newName, String parameter1, String parameter2) {
        this.name = newName;
        this.parameterNames.add(parameter1);
        this.parameterNames.add(parameter2);
    }
    
    public void setId(int newId) {
        this.id = newId;
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

    public int getId() {
        return this.id;
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
        return this.name + " (parametrit: " + this.parameterNames.get(0) + ", " + this.parameterNames.get(1) + ")";
    }

}
