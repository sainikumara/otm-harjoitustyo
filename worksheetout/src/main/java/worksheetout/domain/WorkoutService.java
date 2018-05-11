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
    
    public List<Exercise> getExercisesOfRoutine(Routine routine) {
        List<Exercise> exercises = new ArrayList<>();
        try {
            exercises = this.routineDao.getExercises(routine.getName(), this.loggedIn.getSpreadsheetId());
        } catch (Exception e) {
            return null;
        }
        return exercises;
    }

    public void createRoutine(String name) {
        Routine routine = new Routine(name);
        this.routineToSheet(routine, this.loggedIn.getSpreadsheetId());
    }

    public Exercise createExercise(String name, String parameter1, String parameter2) {
        if (name == null || name.isEmpty() || parameter1 == null || parameter1.isEmpty() || parameter2 == null || parameter2.isEmpty()) {
            return null;
        }
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
