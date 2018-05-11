package worksheetout.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import worksheetout.dao.RoutineDao;
import worksheetout.dao.SheetRoutineDao;
import worksheetout.dao.SheetWorkoutSessionDao;
import worksheetout.dao.UserDao;
import worksheetout.dao.WorkoutSessionDao;

/**
 * The class responsible for the logic of the application
 **/

public class WorkoutService {
    private RoutineDao routineDao;
    private WorkoutSessionDao workoutSessionDao;
    private UserDao userDao;
    private User loggedIn;
    
    public WorkoutService(UserDao newUserDao, RoutineDao newRoutineDao, WorkoutSessionDao newWorkoutSessionDao) {
        this.routineDao = newRoutineDao;
        this.workoutSessionDao = newWorkoutSessionDao;
        this.userDao = newUserDao;
    }

    /**
     * Saving a workout routine to Google Sheets
     * @param routine workout routine to be saved
     * @param spreadsheetId the id of the sheet in which the routine is to be saved
     */
    
    public void routineToSheet(Routine routine, String spreadsheetId) {
        try {
            this.routineDao.save(routine, spreadsheetId);
        } catch (Exception e) {
        }
    }
    
    /**
     * Add one exercise to a routine
     * @param exercise exercise to be added
     * @param routine routine in which the exercise is added
     * @return truea id adding successful, false if not
     */
    public boolean addExerciseToRoutine(Exercise exercise, Routine routine) {
        if (exercise == null) {
            return false;
        }
        try {
            routine.addOneExercise(exercise);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * Get the exercises that have been added to a routine
     * @param routine the routine in which the exercises have been added
     * @return the exercises as a list if successful, null otherwise
     */
    public List<Exercise> getExercisesOfRoutine(Routine routine) {
        List<Exercise> exercises = new ArrayList<>();
        try {
            exercises = this.routineDao.getExercises(routine.getName(), this.loggedIn.getSpreadsheetId());
        } catch (Exception e) {
            return null;
        }
        return exercises;
    }

    /**
     * Create a routine
     * @param name the name for the routine
     */
    public void createRoutine(String name) {
        Routine routine = new Routine(name);
        this.routineToSheet(routine, this.loggedIn.getSpreadsheetId());
    }

    /**
     * Create one exercise
     * @param name name of the exercise as String
     * @param parameter1 first parameter of the exercise as String
     * @param parameter2 second parameter of the exercise as String
     * @return the created exercise if successful, null otherwise
     */
    public Exercise createExercise(String name, String parameter1, String parameter2) {
        if (name == null || name.isEmpty() || parameter1 == null || parameter1.isEmpty() || parameter2 == null || parameter2.isEmpty()) {
            return null;
        }
        Exercise exercise = new Exercise(name, parameter1, parameter2);
        return exercise;
    }

    /**
     * Get the names of the routines that have been saved in a spreadsheet
     * @param spreadsheetId if of the spreadsheet
     * @return the names as a list
     */
    public List<String> getRoutineNames(String spreadsheetId) {
        List<String> routineNames = new ArrayList<>();
        try {
            routineNames = this.routineDao.getRoutineNames(spreadsheetId);
        } catch (Exception e) {
            
        }
        return routineNames;
    }

    /**
     * Get all the routines that have been saved in a spreadsheet
     * @return the routines as a list
     */
    public List<Routine> getRoutines() {
        List<Routine> routines = new ArrayList<>();
        try {
            routines = this.routineDao.getRoutines(this.loggedIn.getSpreadsheetId());
        } catch (Exception e) {
        }

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
        } catch (Exception e) {
        }
    }
    
    /**
     * Add one DoneExercise to a WorkoutSession
     * @param session the WorkoutSession in which to add the DoneExercise
     * @param exercise the exercise based on which the DoneExercise will be created
     * @param firstParameterValue value of the first parameter of the DoneExercise
     * @param secondParameterValue value of the second parameter of the DoneExercise
     * @return true if successful, false if not
     */
    public boolean addDoneExerciseToSession(WorkoutSession session, Exercise exercise, String firstParameterValue, String secondParameterValue) {
        List<Double> parameterValues = new ArrayList<>();
        try {
            parameterValues.add(Double.parseDouble(firstParameterValue));
            parameterValues.add(Double.parseDouble(secondParameterValue));
        } catch (Exception e) {
            return false;
        }
        session.addOneDoneExercise(exercise.getName(), parameterValues);
        return true;
    }
    
    /**
     * Create a new WorkoutSession
     * @param dateAsString the date of the workout session as a string formed as "yyyy-mm-dd"
     * @param routine the routine that the exercise session is based on
     * @return the created WorkoutSession if successful, otherwise null
     */
    public WorkoutSession createWorkoutSession(String dateAsString, Routine routine) {
        Date date = null;
        try {
            date = this.stringToDate(dateAsString);
            if (date == null) {
                return null;
            }
            WorkoutSession session = new WorkoutSession(date, routine);
            return session;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get all the WorkoutSessions for a routine
     * @param routine the routine on which the sessions are based
     * @return the WorkoutSessions as a list if successful, null if not
     */
    public List<WorkoutSession> getWorkoutSessions(Routine routine) {
        List<WorkoutSession> sessions = new ArrayList<>();
        try {
            sessions = this.workoutSessionDao.getWorkoutSessions(routine, this.loggedIn.getSpreadsheetId());
        } catch (Exception e) {
            return null;
        }
        return sessions;
    }
    
    public String dateToString(Date date) {
        if (date != null) {
            return this.workoutSessionDao.parseDateToString(date);
        } else {
            return "";
        }
    }
    
    public Date stringToDate(String dateAsString) {
        return this.workoutSessionDao.parseStringToDate(dateAsString);
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
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
