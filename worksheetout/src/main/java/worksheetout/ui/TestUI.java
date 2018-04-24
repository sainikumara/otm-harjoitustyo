package worksheetout.ui;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
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
import worksheetout.dao.SheetsServiceUtil;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;


public class TestUI {
    private static Sheets sheetsService;
    
    public static void setupSheetService() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }
    
    public static void textUI() {
        Scanner reader = new Scanner(System.in);
        String file = "trainingLog.txt";
        
        try {
            setupSheetService();
        } catch (Exception e) {
            System.out.println("Could not set up Sheets service. Error message: " + e);
        }
        
        System.out.println("\nThis is the text-based user interface for worksheetout app");
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
            
            Exercise exercise = new Exercise(exerciseName, parameter1, parameter2);
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
        
        System.out.println("");
        System.out.println("Tell about your session in (integer) numbers");
        
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
                
        System.out.println("\nGive the id of a Google Sheets spreadsheet in which you want to save your exercise data\n"
                + "(id is a long string of characters in the url of the document \".../d/[ID OF THE DOCUMENT]/edit#gid=0\"):");
        String spreadsheetId = reader.nextLine();
        
        workoutSessionToSheet(workoutSession, spreadsheetId);
        
        try (FileWriter writer = new FileWriter(new File(file))) {
            writer.write(user.getUsername() + ", " + firstRoutine.getName() + ":\n");
            writer.write(workoutSession.toString());
            
            System.out.println("\nA backup of your session is saved in the file " + file);
        } catch (Exception e) {
            System.out.println("\nSomething went wrong with saving your exercises to a file: " + e.getMessage());
        }   
    }
    
    public static void workoutSessionToSheet(WorkoutSession session, String spreadsheetId) {
        String workoutName = session.getRoutine().getName();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sessionDate = dateFormat.format(session.getDate());

        String firstParameter = session.getSessionContents().get(0).getParameters().get(0);
        String secondParameter = session.getSessionContents().get(0).getParameters().get(1);
        
        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange().setRange("A1").setValues(Arrays.asList(Arrays.asList(workoutName, sessionDate), Arrays.asList("", firstParameter, secondParameter))));
        int rowNumber = 3;
        
        for (DoneExercise exercise : session.getSessionContents()) {
            if (!exercise.getParameters().get(0).equals(firstParameter) || !exercise.getParameters().get(1).equals(secondParameter)) {
                data.add(new ValueRange().setRange("A" + rowNumber).setValues(Arrays.asList(Arrays.asList("", exercise.getParameters().get(0), exercise.getParameters().get(1)))));
                rowNumber++;
            }
            
            String exerciseName = exercise.getName();
            String firstParameterValue = "" + exercise.getParameterValue(exercise.getParameters().get(0));
            String secondParameterValue = "" + exercise.getParameterValue(exercise.getParameters().get(1));
            data.add(new ValueRange().setRange("A" + rowNumber).setValues(Arrays.asList(Arrays.asList(exerciseName, firstParameterValue, secondParameterValue))));    
            rowNumber++;
        }
        
        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest().setValueInputOption("RAW").setData(data);
     
        try {
            BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, batchBody).execute();
            System.out.println("\nYour session has been saved on the spreadsheet with the id: " + spreadsheetId);
        } catch (Exception e) {
            System.out.println("\nCould not save to Sheets. Error message: " + e);
        }
    }
    
    public static void main(String[] args) {
        textUI();
    }
}
