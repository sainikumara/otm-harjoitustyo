package worksheetout.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a workout routine that consists of individual exercises
 */

public class Routine {

    private String name;
    private User user;
    private List<Exercise> exercises;
    
    public Routine(String newName, User newUser) {
        this.name = newName;
        this.user = newUser;
        this.exercises = new ArrayList<>();
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
    
    public List<Exercise> getExercises() {
        return this.exercises;
    }
    
    /**
     * Listing the names of the exercises that are part of the routine
     **/
    
    public List<String> getExerciseNames() {
        List<String> exerciseNames = new ArrayList<>();
        for (Exercise exercise : this.exercises) {
            exerciseNames.add(exercise.getName());
            exerciseNames.add("");
        }
       
        return exerciseNames;
    }
    
    /**
     * Listing all the parameters of the exercises that are part of the routine
     **/
    
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
    
    /**
     * Adds one exercise to the routine
     * 
     * @param newExercise new exercise
     **/
    
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
        return this.name.equals(other.name);
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
