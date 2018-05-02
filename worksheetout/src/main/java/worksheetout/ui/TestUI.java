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
import worksheetout.domain.WorkoutService;

public class TestUI {
    
    public static void textUI(WorkoutService workoutService) {
        Scanner scanner = new Scanner(System.in);
               
        System.out.println("\nThis is the text-based user interface for worksheetout app");
        System.out.println("Please choose a name and a username:");
        System.out.print("name: ");
        String name = scanner.nextLine();
        System.out.print("username: ");
        String username = scanner.nextLine();
        User user = new User(username, name);
        
        System.out.println("\nWelcome, " + name + "! Now, let's get started with your first workout routine.");
        System.out.print("Please give a name for the routine: ");
        String routineName = scanner.nextLine();
        Routine routine = new Routine(routineName, user);
        
        System.out.println("Next, add the exercises you want to add to this routine");
        
        while (true) {
            promptForNewExercise(routine, scanner);
            
            System.out.println("Press enter if you want to add another exercise. Otherwise, type any letter and press enter.");
            String loopMore = scanner.nextLine();
            
            if (!loopMore.equals("")) {
                break;
            }
        }
        
        System.out.println("\nGive the id of a Google Sheets spreadsheet in which you want to save your exercise data\n"
            + "(id is a long string of characters in the url of the document \".../d/[ID OF THE DOCUMENT]/edit#gid=0\"):");
        String spreadsheetId = scanner.nextLine();
        
        workoutService.routineToSheet(routine, spreadsheetId);
        
        System.out.println("Your first exercise routine looks like this: " + routine);
        System.out.println("");
        
        System.out.println("Let's start documenting your workouts!");
        
        while (true) {
            WorkoutSession workoutSession = promptForNewSession(routine, scanner);
            workoutService.workoutSessionToSheet(workoutSession, spreadsheetId);
            System.out.println("Press enter if you want to add another session. Otherwise, type any letter and press enter.");
            String loopMore = scanner.nextLine();
            
            if (!loopMore.equals("")) {
                break;
            }
        }     
    }
    
    public static WorkoutSession promptForNewSession(Routine routine, Scanner scanner) {
        System.out.print("Day of the workout session (use format 'yyyy-MM-dd') ");
        String dateAsString = scanner.nextLine();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        
        try {
            date = dateFormat.parse(dateAsString);
        } catch (Exception e) {
            System.out.println("There was an error parsing the date: " + e.getMessage());
        }
        
        WorkoutSession workoutSession = new WorkoutSession(date, routine);
        
        System.out.println("");
        System.out.println("Tell about your session in (integer) numbers");
                
        for (Exercise exercise : workoutSession.getRoutine().getExercises()) {
            List<Integer> parameterValues = new ArrayList<>();           
            
            System.out.print(exercise.getName() + ", " + exercise.getParameters().get(0) + ": ");
            int parameter1Value = Integer.parseInt(scanner.nextLine());
            parameterValues.add(parameter1Value);
            System.out.print(exercise.getName() + ", " + exercise.getParameters().get(1) + ": ");
            int parameter2Value = Integer.parseInt(scanner.nextLine());
            parameterValues.add(parameter2Value);
            
            workoutSession.addOneDoneExercise(exercise.getName(), parameterValues);
            
        }
        return workoutSession;
    }
    
    public static void promptForNewExercise(Routine routine, Scanner scanner) {
        System.out.print("exercise name: ");
        String exerciseName = scanner.nextLine();
        System.out.print("first parameter (eg. kg, speed): ");
        String parameter1 = scanner.nextLine();
        System.out.print("second parameter (eg. repetitions, minutes): ");
        String parameter2 = scanner.nextLine();
            
        Exercise exercise = new Exercise(exerciseName, parameter1, parameter2);
        routine.addOneExercise(exercise);
    }
    
    public static void main(String[] args) {
        WorkoutService workoutService = new WorkoutService();
        textUI(workoutService);
    }
}
