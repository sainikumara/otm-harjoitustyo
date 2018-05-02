package worksheetout.dao;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import worksheetout.domain.Routine;
import worksheetout.domain.WorkoutSession;


public class SheetWorkoutSessionDao implements WorkoutSessionDao {
    private Sheets sheetsService;
    
    public SheetWorkoutSessionDao(Sheets newSheetsService) {
        this.sheetsService = newSheetsService;
    }
    
    public Sheets getSheetsService() {
        return this.sheetsService;
    }
    
    @Override
    public void save(WorkoutSession session, String spreadsheetId) throws Exception {
        ValueRange appendBody = new ValueRange().setValues(Arrays.asList(Arrays.asList(session.getDateAndExerciseParameterValues().toArray())));
        AppendValuesResponse appendResult = sheetsService.spreadsheets().values().append(spreadsheetId, "A4", appendBody).setValueInputOption("RAW").setInsertDataOption("INSERT_ROWS").setIncludeValuesInResponse(true).execute();
    }

    @Override
    public List<WorkoutSession> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
