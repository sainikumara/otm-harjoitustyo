package worksheetout.ui;

import java.util.ArrayList;
import java.util.List;
import worksheetout.domain.DoneExercise;
import worksheetout.domain.Exercise;
import worksheetout.domain.Routine;
import worksheetout.domain.WorkoutSession;
import worksheetout.domain.User;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUI {
    
    public static void textUI() {
        Scanner reader = new Scanner(System.in);
        String file = "trainingLog.txt";
        
        System.out.println("This is the text-based user interface for worksheetout app");
        System.out.println("Please choose a name and a username:");
        System.out.print("name: ");
        String name = reader.nextLine();
        System.out.print("username: ");
        String username = reader.nextLine();
        User user = new User(username, name);
        
        System.out.println("\nWelcome, " + name + "! Now, let's get started with your first workout routine.");
        System.out.print("Please give a name for the routine: ");
        String routineName = reader.nextLine();
        Routine firstRoutine = new Routine(routineName, user);
        
        System.out.println("Next, add the exercises you want to add to this routine");
        
        while (true) {
            System.out.print("exercise name: ");
            String exerciseName = reader.nextLine();
            System.out.print("first parameter (eg. kg, speed): ");
            String parameter1 = reader.nextLine();
            System.out.print("second parameter (eg. repetitions, minutes): ");
            String parameter2 = reader.nextLine();
            
            Exercise exercise = new Exercise(exerciseName, user, parameter1, parameter2);
            firstRoutine.addOneExercise(exercise);
            
            System.out.println("Press enter if you want to add another exercise. Otherwise, type any letter and press enter.");
            String loopMore = reader.nextLine();
            
            if (!loopMore.equals("")) {
                break;
            }
        }
        
        System.out.println("Your first exercise routine looks like this: " + firstRoutine);
        System.out.println("");
        
        System.out.println("Let's hit the gym!");
        
        System.out.print("What day is it? (use format 'yyyy-MM-dd') ");
        String dateAsString = reader.nextLine();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Date date = new Date();
        
        try {
            date = dateFormat.parse(dateAsString);
        } catch (Exception e) {
            System.out.println("There was an error parsing the date: " + e.getMessage());
        }
        
        WorkoutSession workoutSession = new WorkoutSession(date, firstRoutine);
                
        for (Exercise exercise : workoutSession.getRoutine().getExercises()) {
            List<Integer> parameterValues = new ArrayList<>();           
            
            System.out.print(exercise.getName() + ", " + exercise.getParameters().get(0) + ": ");
            int parameter1Value = Integer.parseInt(reader.nextLine());
            parameterValues.add(parameter1Value);
            System.out.print(exercise.getName() + ", " + exercise.getParameters().get(1) + ": ");
            int parameter2Value = Integer.parseInt(reader.nextLine());
            parameterValues.add(parameter2Value);
            
            workoutSession.addOneDoneExercise(exercise.getName(), parameterValues);
            
        }
        
        System.out.println(workoutSession.toString());
        
        try (FileWriter writer = new FileWriter(new File(file))) {
            writer.write(user.getUsername() + ", " + firstRoutine.getName() + ":\n");
            writer.write(workoutSession.toString());
            
            System.out.println("Your exercises are now saved in the file " + file);
        } catch (Exception e) {
            System.out.println("Something went wrong with saving your exercises to a file: " + e.getMessage());
        }   
    }
    
    public static void main(String[] args) {
        textUI();
        
        
//        User user = new User("tester", "The Real Tester");
//        
//        Exercise squat = new Exercise("squat", user, "kg", "repetitions");
//        Exercise deadlift = new Exercise("deadlift", user, "kg", "repetitions");
//        Exercise calfRaise = new Exercise("calf raise", user, "kg", "repetitions");
//        
//        System.out.println(squat);
//        System.out.println(deadlift);
//        System.out.println(calfRaise);
//        System.out.println("");
//        
//        List<Exercise> legdayExercises = new ArrayList<>();
//        legdayExercises.add(squat);
//        legdayExercises.add(deadlift);
//        legdayExercises.add(calfRaise);
//        
//        Routine legdayRoutine = new Routine("legday", user);
//        legdayRoutine.setExecises(legdayExercises);
//        System.out.println(legdayRoutine);
//        
//        List<Integer> squatValues = new ArrayList<>();
//        squatValues.add(50);
//        squatValues.add(20);
//        DoneExercise doneSquats = new DoneExercise(legdayRoutine.getExercises().get(0).getName(), user, legdayRoutine.getExercises().get(0).getParameters(), squatValues);
//        List<Integer> deadliftValues = new ArrayList<>();
//        deadliftValues.add(70);
//        deadliftValues.add(15);
//        DoneExercise doneDeadlifts = new DoneExercise(legdayRoutine.getExercises().get(1).getName(), user, legdayRoutine.getExercises().get(1).getParameters(), deadliftValues);
//        
//        
//        System.out.println(doneSquats);
//        System.out.println(doneDeadlifts);
    }
}
