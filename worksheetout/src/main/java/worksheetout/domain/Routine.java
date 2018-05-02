package worksheetout.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a workout routine that consists of individual exercises
 */

public class Routine {

    private int id;
    private String name;
    private User user;
    private List<Exercise> exercises;

    public Routine(int id, String newName, User newUser) {
        this.id = id;
        this.name = newName;
        this.user = newUser;
        this.exercises = new ArrayList<>();
    }
    
    public Routine(String newName, User newUser) {
        this.name = newName;
        this.user = newUser;
        this.exercises = new ArrayList<>();
    }
    
    public void setId(int newId) {
        this.id = newId;
    }
    
    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public User getUser() {
        return this.user;
    }

    public int getId() {
        return this.id;
    }
    
    public List<Exercise> getExercises() {
        return this.exercises;
    }
    
    public List<String> getExerciseNames() {
        List<String> exerciseNames = new ArrayList<>();
        for (Exercise exercise : this.exercises) {
            exerciseNames.add(exercise.getName());
            exerciseNames.add("");
        }
       
        return exerciseNames;
    }
    
    public List<String> getExerciseParameters() {
        List<String> exerciseParameters = new ArrayList<>();
             
        for (Exercise exercise : this.exercises) {
            exerciseParameters.add(exercise.getParameters().get(0));
            exerciseParameters.add(exercise.getParameters().get(1));
        }

        return exerciseParameters;
    }
    
    public void setExecises(List<Exercise> newExercises) {
        this.exercises = newExercises;
    }
    
    public void addOneExercise(Exercise newExercise) {
        this.exercises.add(newExercise);
    }
    
    public Exercise getOneExercise(String name) {
        Exercise exerciseToFind = new Exercise(name);
        return this.exercises.get(this.exercises.indexOf(exerciseToFind));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Routine)) {
            return false;
        }
        Routine other = (Routine) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        String routineAsAString = this.name + ": \n";
        
        for (Exercise exercise : this.exercises) {
            routineAsAString += exercise.toString() + "\n";
        }
        
        return routineAsAString;
    }
}
