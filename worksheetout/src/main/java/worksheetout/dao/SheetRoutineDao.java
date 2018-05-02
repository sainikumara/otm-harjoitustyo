package worksheetout.dao;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import worksheetout.domain.Routine;


public class SheetRoutineDao implements RoutineDao {
    private Sheets sheetsService;
    
    public SheetRoutineDao(Sheets newSheetsService) {
        this.sheetsService = newSheetsService;
    }
    
    public Sheets getSheetsService() {
        return this.sheetsService;
    }
    
    @Override
    public void save(Routine routine, String spreadsheetId) throws Exception {
        List<ValueRange> data = new ArrayList<>();
        
        data.add(new ValueRange().setRange("A1").setValues(Arrays.asList(Arrays.asList(routine.getName()))));       
        data.add(new ValueRange().setRange("B2").setValues(Arrays.asList(Arrays.asList(routine.getExerciseNames().toArray()))));
        data.add(new ValueRange().setRange("B3").setValues(Arrays.asList(Arrays.asList(routine.getExerciseParameters().toArray()))));
        
        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest().setValueInputOption("RAW").setData(data);
        
        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, batchBody).execute();
    }

    @Override
    public List<Routine> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
