package worksheetout.domain;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Properties;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import worksheetout.dao.FileUserDao;
import worksheetout.dao.SheetRoutineDao;
import worksheetout.dao.SheetWorkoutSessionDao;
import worksheetout.dao.SheetsServiceUtil;

public class WorkoutServiceIntegrationTest {
    private static Sheets sheetsService;
    private static final String SPREADSHEET_ID = "10sJVMDAzjjn4eq7cACGiQ7sske1lVMmFc3sliJkvluI";
    private static WorkoutService workoutService;
    private static Routine routine;
    private static Exercise exercise;

    @BeforeClass
    public static void setup() throws GeneralSecurityException, IOException, Exception {
        sheetsService = SheetsServiceUtil.getSheetsService();
        sheetsService.spreadsheets().get(SPREADSHEET_ID).clear();
        
        Properties properties = new Properties();
        
        File config = new File("config.properties");
        
        if (!config.exists()) {
            config.createNewFile();
            Path path = Paths.get("config.properties");
            Files.write(path, Arrays.asList("userFile=users.txt"), Charset.forName("UTF-8"));
        }

        properties.load(new FileInputStream("config.properties"));
        String userFile = properties.getProperty("userFile");
        FileUserDao userDao = new FileUserDao(userFile);
        
        SheetRoutineDao routineDao = new SheetRoutineDao(sheetsService);
        routineDao.setSpreadsheet(SPREADSHEET_ID);
        
        SheetWorkoutSessionDao workoutSessionDao = new SheetWorkoutSessionDao(sheetsService);
        workoutService = new WorkoutService(userDao, routineDao, workoutSessionDao);
        workoutService.createUser("Terttu", SPREADSHEET_ID);
        workoutService.login("Terttu");
        routine = new Routine("Leg day");
        exercise = workoutService.createExercise("Squat", "kg", "reps");
    }
    
    @Test
    public void routineToSheetWorks() {
        workoutService.createRoutine("Leg day");
        assertTrue(workoutService.getRoutines().contains(routine));
    }
    
    @Test
    public void addExerciseToRoutineWorks() {
        workoutService.addExerciseToRoutine(exercise, routine);
        workoutService.routineToSheet(routine, SPREADSHEET_ID);
        assertTrue(workoutService.getExercisesOfRoutine(routine).contains(exercise));
    }
    
    @Test
    public void workoutSessionCreatingAndSavingWorks() {
        WorkoutSession session = workoutService.createWorkoutSession("2018-02-03", routine);
        workoutService.addDoneExerciseToSession(session, exercise, "50", "30");
        workoutService.workoutSessionToSheet(session, SPREADSHEET_ID);
        assertTrue(workoutService.getWorkoutSessions(routine).contains(session));
    }
    
    @Test
    public void noSessionCreatedWithEmptyDateInput() {
        WorkoutSession session = workoutService.createWorkoutSession("", routine);
        assertNull(session);
    }
    
    @Test
    public void noSessionCreatedWithIncorrectDateInput() {
        WorkoutSession session = workoutService.createWorkoutSession("yesterday", routine);
        assertNull(session);
    }
    
    @Test
    public void noDoneExerciseAddedToSessionWithIncorrectAttributes() {
        WorkoutSession session = workoutService.createWorkoutSession("2018-05-01", routine);
        workoutService.addDoneExerciseToSession(session, exercise, "", "kg");
        assertTrue(session.getSessionContents().isEmpty());
    }
}
