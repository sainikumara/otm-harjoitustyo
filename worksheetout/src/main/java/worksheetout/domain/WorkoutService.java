package worksheetout.domain;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
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
            setupSheetService();
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
    
    public void setupSheetService() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }
    
    /**
     * Saving a workout routine to Google Sheets
     * @param routine
     * @param spreadsheetId 
     */
    
    public void routineToSheet(Routine routine, String spreadsheetId) {
        try {
            this.routineDao.save(routine, spreadsheetId);
            System.out.println("\nYour routine has been saved on the spreadsheet with the id: " + spreadsheetId + "\n");
        } catch (Exception e) {
            System.out.println("\nCould not save to Sheets. Error message: " + e + "\n");
        }
    }
    
    /**
     * Saving a workout session to Google Sheets
     * @param session
     * @param spreadsheetId 
     */
    
    public void workoutSessionToSheet(WorkoutSession session, String spreadsheetId) {
      
        ValueRange appendBody = new ValueRange().setValues(Arrays.asList(Arrays.asList(session.getDateAndExerciseParameterValues().toArray())));

        try {
            AppendValuesResponse appendResult = sheetsService.spreadsheets().values().append(spreadsheetId, "A4", appendBody).setValueInputOption("RAW").setInsertDataOption("INSERT_ROWS").setIncludeValuesInResponse(true).execute();
            System.out.println("\nYour session has been saved on the spreadsheet with the id: " + spreadsheetId + "\n");
        } catch (Exception e) {
            System.out.println("\nCould not save to Sheets. Error message: " + e + "\n");
        }
    }
}
