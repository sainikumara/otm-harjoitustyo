package worksheetout.dao;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
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
    
    /**
     * Create a new worksheet in a Google spreadsheet, representing a routine and having the same name
     * @param routineName the name of the routine that will be saved on the spreadsheet
     * @param spreadsheetId the id of the spreadsheet in which to create the new worksheet
     * @throws Exception 
     */
    private void createNewSheet(String routineName, String spreadsheetId) throws Exception {
        if (this.getSheetIdBasedOnTitle(routineName, spreadsheetId) == null) {        
            List<Request> requests = new ArrayList<>();
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(routineName))));
            BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
            this.sheetsService.spreadsheets().batchUpdate(spreadsheetId, body).execute();
        }
    }
    
    /**
     * Save the names and parameters of the exercises in a routine on the worksheet that represents the routine
     * @param routine the routine that the sheet represents, also the name of the worksheet on which to save the data
     * @param spreadsheetId the id of the spreadsheet in which to save the data
     * @throws Exception 
     */
    private void saveExerciseNamesAndParameters(Routine routine, String spreadsheetId) throws Exception {
        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange().setRange("'" + routine.getName() + "'!B1").setValues(Arrays.asList(Arrays.asList(routine.getExerciseNames().toArray()))));
        data.add(new ValueRange().setRange("'" + routine.getName() + "'!B2").setValues(Arrays.asList(Arrays.asList(routine.getExerciseParameters().toArray()))));
        
        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest().setValueInputOption("RAW").setData(data);
        sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, batchBody).execute();
    }
    
    @Override
    public void setSpreadsheet(String spreadsheetId) throws Exception {
        this.spreadsheet = this.sheetsService.spreadsheets().get(spreadsheetId).execute();       
    }
    
    /**
     * Get all the worksheets in a Google spreadsheet
     * @param spreadsheetId the id of the spreadsheet
     * @return the worksheets as a list
     * @throws Exception 
     */
    @Override
    public List<Sheet> getWorksheets(String spreadsheetId) throws Exception {
        this.setSpreadsheet(spreadsheetId);
        
        List<Sheet> sheets = this.spreadsheet.getSheets();
        return sheets;
    }
    
    /**
     * Get all the routines that have been saved in a spreadsheet
     * @param spreadsheetId the id of the spreadsheet
     * @return the routines as a list
     * @throws Exception 
     */
    @Override
    public List<Routine> getRoutines(String spreadsheetId) throws Exception {
        List<Sheet> sheets = this.getWorksheets(spreadsheetId);
        List<Routine> routines = new ArrayList<>();
        
        for (Sheet sheet : sheets) {
            if (!sheet.getProperties().getTitle().startsWith("Sheet")) {
                routines.add(this.getOneRoutine(sheet, spreadsheetId));
            }
        }
        return routines;
    }
    
    /**
     * Get the routine that has been saved on a particular worksheet
     * @param sheet the sheet from which to extract the routine's data
     * @param spreadsheetId the id of the spreadsheet
     * @return the routine from the worksheet
     * @throws Exception 
     */
    @Override
    public Routine getOneRoutine(Sheet sheet, String spreadsheetId) throws Exception {
        String routineName = sheet.getProperties().getTitle();
        Routine routine = new Routine(routineName);
        if (this.getExercises(routineName, spreadsheetId) == null) {
            return routine;
        }
        routine.setExecises(this.getExercises(routineName, spreadsheetId));
        return routine;
    }
    
    /**
     * Get all the exercises in certain range, eg. a worksheet
     * @param range the range from which to retrieve the data for the exercises, in A1 format
     * @param spreadsheetId the id of the spreadsheet
     * @return the exercises as a list
     * @throws Exception 
     */
    @Override
    public List<Exercise> getExercises(String range, String spreadsheetId) throws Exception {
        List<List<Object>> columns = this.getSheetValues(range, spreadsheetId);
        
        if (columns == null) {
            return null;
        }
        List<Exercise> exercises = new ArrayList<>();
        
        for (int i = 1; i < columns.size(); i = i + 2) {
            if (!columns.get(i).get(0).toString().isEmpty()) {
                Exercise exercise = this.getOneExercise(columns.get(i), columns.get(i + 1));
                exercises.add(exercise);
            }
        }
        return exercises;
    }
    
    /**
     * Pick the data needed to form one Exercise object, in order to get that Exercise
     * @param column1 list of objects (that has been formed out of one column in a worksheet)
     * @param column2 list of objects (that has been formed out of the column on the right hand side of column1)
     * @return return one Exercise object
     * @throws Exception 
     */
    @Override
    public Exercise getOneExercise(List<Object> column1, List<Object> column2) throws Exception {
        String exerciseName = "";
        if (column1.get(0) != null) {
            exerciseName = column1.get(0).toString();
        }
        String firstParameter = "";
        if (column1.get(1) != null) {
            firstParameter = column1.get(1).toString();
        }
        String secondParameter = "";
        if (column2.get(1) != null) {
            secondParameter = column2.get(1).toString();
        }
        Exercise exercise = new Exercise(exerciseName, firstParameter, secondParameter);
        
        return exercise;
    }
    
    /**
     * Get all the data from a certain range in a spreadsheet, eg. a worksheet
     * @param range the range of the desired data, in A1 format
     * @param spreadsheetId the id of the spreadsheet
     * @return a list of lists of objects, that is formed of the data saved in the columns within the range
     * @throws Exception 
     */
    @Override
    public List<List<Object>> getSheetValues(String range, String spreadsheetId) throws Exception {
        ValueRange response = this.sheetsService.spreadsheets().values().get(spreadsheetId, range).setMajorDimension("COLUMNS").execute();
        List<List<Object>> columns = response.getValues();
        
        if (columns != null && columns.size() != 0) {
            return columns;
        } else {
            return null;
        }
    }
    
    
    /**
     * Get the names of all the routines that have been saved in a spreadsheet
     * @param spreadsheetId the id of the spreadsheet
     * @return return the names of the routines as a list
     * @throws Exception 
     */
    @Override
    public List<String> getRoutineNames(String spreadsheetId) throws Exception {
        List<Sheet> sheets = this.getWorksheets(spreadsheetId);
        List<String> routineNames = new ArrayList<>();
        
        for (Sheet sheet : sheets) {
            if (!sheet.getProperties().getTitle().startsWith("Sheet")) {
                routineNames.add(sheet.getProperties().getTitle());
            }
        }
        return routineNames;
    }
    
    /**
     * Get the id of the worksheet based on its title
     * @param sheetTitle the title of a sheet
     * @param spreadsheetId the id of the spreadsheet
     * @return return the id of the worksheet if successful, null if not
     * @throws Exception 
     */
    @Override
    public Integer getSheetIdBasedOnTitle(String sheetTitle, String spreadsheetId) throws Exception {
        List<Sheet> sheets = this.getWorksheets(spreadsheetId);
        
        for (Sheet sheet : sheets) {
            if (sheetTitle.equals(sheet.getProperties().getTitle())) {
                return sheet.getProperties().getSheetId();
            }
        }
        return null;
    }

}
