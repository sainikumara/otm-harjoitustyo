package worksheetout.dao;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import worksheetout.domain.Exercise;
import worksheetout.domain.Routine;

/**
 * The class for handling the connection between WorkoutSessions and Google Sheets
 */

public class SheetRoutineDao implements RoutineDao {
    private Sheets sheetsService;
    private Spreadsheet spreadsheet;
    
    public SheetRoutineDao(Sheets newSheetsService) {
        this.sheetsService = newSheetsService;
        this.spreadsheet = null;
    }
    
    public Sheets getSheetsService() {
        return this.sheetsService;
    }
    
    
    /**
     * Save a workout routine to a Google Sheets document
     * @param routine
     * @param spreadsheetId
     * @throws Exception 
     */
    
    @Override
    public void save(Routine routine, String spreadsheetId) throws Exception {
        this.createNewSheet(routine.getName(), spreadsheetId);
        this.saveExerciseNamesAndParameters(routine, spreadsheetId);
    }
    
    private void createNewSheet(String routineName, String spreadsheetId) throws Exception {
        if (this.getSheetIdBasedOnTitle(routineName, spreadsheetId) == null) {        
            List<Request> requests = new ArrayList<>();
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(routineName))));
            BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
            this.sheetsService.spreadsheets().batchUpdate(spreadsheetId, body).execute();
        }
    }
    
    private void saveExerciseNamesAndParameters(Routine routine, String spreadsheetId) throws Exception {
        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange().setRange("'" + routine.getName() + "'!B1").setValues(Arrays.asList(Arrays.asList(routine.getExerciseNames().toArray()))));
        data.add(new ValueRange().setRange("'" + routine.getName() + "'!B2").setValues(Arrays.asList(Arrays.asList(routine.getExerciseParameters().toArray()))));
        
        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest().setValueInputOption("RAW").setData(data);
        sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, batchBody).execute();
    }
    
    public void setSpreadsheet(String spreadsheetId) throws Exception {
        if (this.spreadsheet == null) {
            this.spreadsheet = this.sheetsService.spreadsheets().get(spreadsheetId).execute();
        }
    }
    
    public List<Sheet> getWorkouts(String spreadsheetId) throws Exception {
        this.setSpreadsheet(spreadsheetId);
        
        List<Sheet> sheets = this.spreadsheet.getSheets();
        System.out.println("Sheets: " + sheets);
        
        return sheets;
    }
    
    public List<Routine> getRoutines(String spreadsheetId) throws Exception {
        List<Sheet> sheets = this.getWorkouts(spreadsheetId);
        List<Routine> routines = new ArrayList<>();
        
        for (Sheet sheet : sheets) {
            if (!sheet.getProperties().getTitle().startsWith("Sheet")) {
                routines.add(this.getOneRoutine(sheet, spreadsheetId));
            }
        }
        return routines;
    }
    
    public Routine getOneRoutine(Sheet sheet, String spreadsheetId) throws Exception {
        String routineName = sheet.getProperties().getTitle();
        System.out.println("Routine name: " + routineName);
        Routine routine = new Routine(routineName);
        System.out.println("routine: " + routine);
        routine.setExecises(this.getExercises(routineName, spreadsheetId));
        System.out.println("routine with exercises: " + routine);
        return routine;
    }
    
    public List<Exercise> getExercises(String range, String spreadsheetId) throws Exception {
        List<List<Object>> columns = this.getSheetValues(range, spreadsheetId);
        List<Exercise> exercises = new ArrayList<>();
        
        for (int i = 1; i < columns.size(); i = i + 2) {
            if (!columns.get(i).get(0).toString().isEmpty()) {
                Exercise exercise = this.getOneExercise(columns.get(i), columns.get(i + 1));
                System.out.println("Exercise: " + exercise);
                exercises.add(exercise);
            }
	}
        return exercises;
    }
    
    public Exercise getOneExercise(List<Object> column1, List<Object> column2) throws Exception {
        String exerciseName = column1.get(0).toString();
        System.out.println("Exercise name: " + exerciseName);
        String firstParameter = column1.get(1).toString();
        System.out.println("Parameter1: " + firstParameter);
        String secondParameter = column2.get(1).toString();
        System.out.println("Parameter2: " + secondParameter);
        Exercise exercise = new Exercise(exerciseName, firstParameter, secondParameter);
        
        return exercise;
    }
    
    public List<List<Object>> getSheetValues(String range, String spreadsheetId) throws Exception {
        ValueRange response = this.sheetsService.spreadsheets().values().get(spreadsheetId, range).setMajorDimension("COLUMNS").execute();
        List<List<Object>> columns = response.getValues();
        
        if (columns != null && columns.size() != 0) {
            return columns;
        } else {
            return null;
        }
    }
    
    public List<String> getRoutineNames(String spreadsheetId) throws Exception {
        List<Sheet> sheets = this.getWorkouts(spreadsheetId);
        List<String> routineNames = new ArrayList<>();
        
        for (Sheet sheet : sheets) {
            if (!sheet.getProperties().getTitle().startsWith("Sheet")) {
                routineNames.add(sheet.getProperties().getTitle());
            }
        }
        return routineNames;
    }
    
    public Integer getSheetIdBasedOnTitle(String sheetTitle, String spreadsheetId) throws Exception {
        List<Sheet> sheets = this.getWorkouts(spreadsheetId);
        
        for (Sheet sheet : sheets) {
            if (sheetTitle.equals(sheet.getProperties().getTitle())) {
                return sheet.getProperties().getSheetId();
            }
        }
        return null;
    }

}
