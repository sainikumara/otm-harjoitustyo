package worksheetout.dao;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.Arrays;
import worksheetout.domain.WorkoutSession;

/**
 * The class for handling the connection of WorkoutSessions and Google Sheets
 */

public class SheetWorkoutSessionDao implements WorkoutSessionDao {
    private Sheets sheetsService;
    
    public SheetWorkoutSessionDao(Sheets newSheetsService) {
        this.sheetsService = newSheetsService;
    }
    
    /**
     * Save a workout session to a Google Sheets document
     * @param session the workout session to be saved
     * @param spreadsheetId the id of the sheets document in which the session is to be saved
     * @throws Exception 
     */
    
    @Override
    public void save(WorkoutSession session, String spreadsheetId) throws Exception {
        ValueRange appendBody = new ValueRange().setValues(Arrays.asList(Arrays.asList(session.getDateAndExerciseParameterValues().toArray())));
        AppendValuesResponse appendResult = sheetsService.spreadsheets().values().append(spreadsheetId, "A4", appendBody).setValueInputOption("RAW").setInsertDataOption("INSERT_ROWS").setIncludeValuesInResponse(true).execute();
    }
}
