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
import worksheetout.domain.Routine;

/**
 * The class responsible for the logic of the application
 **/

public class WorkoutService {
    private Sheets sheetsService;
    private RoutineDao routineDao;
    private WorkoutSessionDao workoutSessionDao;
    
    public WorkoutService() {
        try {
            setupSheetsService();
        } catch (Exception e) {
            System.out.println("Could not set up Sheets service. Error message: " + e);
        }
        this.routineDao = new SheetRoutineDao(this.sheetsService);
        this.workoutSessionDao = new SheetWorkoutSessionDao(this.sheetsService);             
    }
    
    /**
     * Setting up Google Sheets Service
     * @throws GeneralSecurityException
     * @throws IOException 
     */
    
    public void setupSheetsService() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }
    
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
    
    
    public List<String> getRoutineNames(String spreadsheetId) {
        SheetRoutineDao sheetRoutineDao = new SheetRoutineDao(this.sheetsService);
        List<String> routineNames = new ArrayList<>();
        try {
            routineNames = sheetRoutineDao.getRoutineNames(spreadsheetId);
        } catch (Exception e) {
            
        }
        return routineNames;
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
}
