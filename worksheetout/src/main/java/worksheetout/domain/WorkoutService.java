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
import worksheetout.domain.Routine;

public class WorkoutService {
    private Sheets sheetsService;
    private RoutineDao routineDao;
    
    public WorkoutService() {
        try {
            setupSheetService();
        } catch (Exception e) {
            System.out.println("Could not set up Sheets service. Error message: " + e);
        }
        routineDao = new SheetRoutineDao(sheetsService);
    }
    
    public void setupSheetService() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }
    
    public Routine createRoutine(String routineName, User user) {
        Routine routine = new Routine(routineName, user);
        return routine;
    }
    
    public void routineToSheet(Routine routine, String spreadsheetId) {
        try {
            this.routineDao.save(routine, spreadsheetId);
            System.out.println("\nYour routine has been saved on the spreadsheet with the id: " + spreadsheetId + "\n");
        } catch (Exception e) {
            System.out.println("\nCould not save to Sheets. Error message: " + e + "\n");
        }
    }
    
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