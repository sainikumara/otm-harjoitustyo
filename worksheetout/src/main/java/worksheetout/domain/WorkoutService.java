package worksheetout.domain;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import worksheetout.dao.RoutineDao;
import worksheetout.dao.SheetRoutineDao;
import worksheetout.dao.SheetsServiceUtil;
import worksheetout.dao.WorkoutSessionDao;
import worksheetout.dao.SheetWorkoutSessionDao;
import worksheetout.dao.UserDao;
import worksheetout.domain.Routine;

/**
 * The class responsible for the logic of the application
 **/

public class WorkoutService {
    private Sheets sheetsService;
    private SheetRoutineDao routineDao;
    private SheetWorkoutSessionDao workoutSessionDao;
    private UserDao userDao;
    private User loggedIn;
    
    public WorkoutService(UserDao newUserDao, SheetRoutineDao newRoutineDao, SheetWorkoutSessionDao newWorkoutSessionDao) {
//        try {
//            setupSheetsService();
//        } catch (Exception e) {
//            System.out.println("Could not set up Sheets service. Error message: " + e);
//        }
        this.routineDao = newRoutineDao;
        this.workoutSessionDao = newWorkoutSessionDao;
        this.userDao = newUserDao;
    }
    
//    /**
//     * Setting up Google Sheets Service
//     * @throws GeneralSecurityException
//     * @throws IOException 
//     */
//    
//    public void setupSheetsService() throws GeneralSecurityException, IOException {
//        sheetsService = SheetsServiceUtil.getSheetsService();
//    }


    /**
     * Saving a workout routine to Google Sheets
     * @param routine workout routine to be saved
     * @param spreadsheetId the id of the sheet in which the routine is to be saved
     */
    
    public void routineToSheet(Routine routine, String spreadsheetId) {
        try {
            this.routineDao.save(routine, spreadsheetId);
            System.out.println("\nYour routine has been saved on the spreadsheet with the id: " + spreadsheetId + "\n");
        } catch (Exception e) {
            System.out.println("\nCould not save to Sheets. Error message: " + e + "\n");
        }
    }
    
    public void addExerciseToRoutine(Exercise exercise, Routine routine) {
        routine.addOneExercise(exercise);
    }

    public Routine createRoutine(String name) {
        Routine routine = new Routine(name);
        return routine;
    }

    public Exercise createExercise(String name, String parameter1, String parameter2) {
        Exercise exercise = new Exercise(name, parameter1, parameter2);
        return exercise;
    }

    public List<String> getRoutineNames(String spreadsheetId) {
        List<String> routineNames = new ArrayList<>();
        try {
            routineNames = this.routineDao.getRoutineNames(spreadsheetId);
        } catch (Exception e) {
            
        }
        return routineNames;
    }

    public List<Routine> getRoutines() {
        List<Routine> routines = new ArrayList<>();
        try {
            System.out.println("SpreadsheetId: " + this.loggedIn.getSpreadsheetId());
            routines = this.routineDao.getRoutines(this.loggedIn.getSpreadsheetId());
        } catch (Exception e) {
            
        }

        System.out.println("Routines: " + routines);
        return routines;
    }

    /**
     * Saving a workout session to Google Sheets
     * @param session workout session to be saved
     * @param spreadsheetId the id of the sheet in which the session is to be saved
     */

    public void workoutSessionToSheet(WorkoutSession session, String spreadsheetId) {
        try {
            this.workoutSessionDao.save(session, spreadsheetId);
            System.out.println("\nYour session has been saved on the spreadsheet with the id: " + spreadsheetId + "\n");
        } catch (Exception e) {
            System.out.println("\nCould not save to Sheets. Error message: " + e + "\n");
        }
    }

    /**
    * Logging in
    * 
    * @param   username
    * 
    * @return true if username exists, otherwise false 
    */    

    public boolean login(String username) {
        User user = this.userDao.findByUsername(username);
        if (user == null) {
            return false;
        }
        
        this.loggedIn = user;
        
        return true;
    }
    
    /**
    * The user that is logged in
    * 
    * @return the logged in user 
    */   
    
    public User getLoggedUser() {
        return this.loggedIn;
    }
   
    /**
    * Logging out
    */  
    
    public void logout() {
        this.loggedIn = null;  
    }
    
    /**
    * Creating a new user
    * 
    * @param   username   username
    * @param   spreadsheetId   spreadsheet id
    * 
    * @return true if user successfully created, otherwise false 
    */ 
    
    public boolean createUser(String username, String spreadsheetId)  {   
        if (this.userDao.findByUsername(username) != null) {
            return false;
        }
        User user = new User(username, spreadsheetId);
        try {
            this.userDao.create(user);
        } catch(Exception e) {
            return false;
        }

        return true;
    }
}
