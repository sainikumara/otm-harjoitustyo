package worksheetout.dao;

import com.google.api.services.sheets.v4.model.Sheet;
import java.util.List;
import worksheetout.domain.Exercise;
import worksheetout.domain.Routine;

public interface RoutineDao {

    List<Exercise> getExercises(String range, String spreadsheetId) throws Exception;

    Exercise getOneExercise(List<Object> column1, List<Object> column2) throws Exception;

    Routine getOneRoutine(Sheet sheet, String spreadsheetId) throws Exception;

    List<String> getRoutineNames(String spreadsheetId) throws Exception;

    List<Routine> getRoutines(String spreadsheetId) throws Exception;

    Integer getSheetIdBasedOnTitle(String sheetTitle, String spreadsheetId) throws Exception;

    List<List<Object>> getSheetValues(String range, String spreadsheetId) throws Exception;

    List<Sheet> getWorksheets(String spreadsheetId) throws Exception;

    /**
     * Save a workout routine to a Google Sheets document
     * @param routine
     * @param spreadsheetId
     * @throws Exception
     */
    void save(Routine routine, String spreadsheetId) throws Exception;

    void setSpreadsheet(String spreadsheetId) throws Exception;
    
}
