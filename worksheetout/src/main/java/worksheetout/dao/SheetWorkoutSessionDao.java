package worksheetout.dao;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import worksheetout.domain.Exercise;
import worksheetout.domain.Routine;
import worksheetout.domain.WorkoutSession;

/**
 * The class for handling the connection between WorkoutSessions and Google Sheets
 */

public class SheetWorkoutSessionDao implements WorkoutSessionDao {
    private Sheets sheetsService;
    private Spreadsheet spreadsheet;
    private SheetRoutineDao routineDao;

    
    public SheetWorkoutSessionDao(Sheets newSheetsService) {
        this.sheetsService = newSheetsService;
        this.spreadsheet = null;

    }
    
    public void setSpreadsheet(String spreadsheetId) throws Exception {
        this.spreadsheet = this.sheetsService.spreadsheets().get(spreadsheetId).execute();       
    }
    
    /**
     * Save a workout session to a Google Sheets document
     * @param session the workout session to be saved
     * @param spreadsheetId the id of the sheets document in which the session is to be saved
     * @throws Exception 
     */
    
    @Override
    public void save(WorkoutSession session, String spreadsheetId) throws Exception {
        ValueRange appendBody = new ValueRange().setValues(Arrays.asList(Arrays.asList(session
                .getDateAndExerciseParameterValues().toArray())));
        System.out.println("'" + session.getRoutine().getName() + "'!A3");
        System.out.println("TESTING");
        sheetsService.spreadsheets().values()
                .append(spreadsheetId, "'" + session.getRoutine().getName() + "'!A3", appendBody)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true).execute();
    }
    
    public List<WorkoutSession> getWorkoutSessions(Routine routine, String spreadsheetId) throws Exception {
        List<List<Object>> rows = this.getSheetValues(routine.getName(), spreadsheetId);
        if (rows == null) {
            return null;
        }
        List<WorkoutSession> sessions = new ArrayList<>();
        for (int i = 2; i < rows.size(); i++) {
            if (!rows.get(i).get(0).toString().isEmpty()) {
                WorkoutSession session = this.getOneSession(rows.get(i), routine);
                sessions.add(session);
            }
        }
        return sessions;
    }
    
    private WorkoutSession getOneSession(List<Object> row, Routine routine) throws Exception {
        WorkoutSession session = new WorkoutSession(this.parseStringToDate(row.get(0).toString()), routine);
        for (int i = 1; i <= routine.getExerciseParameters().size(); i = i + 2) {
            Exercise exercise = routine.getExercises().get((i - 1) / 2);
            List<Double> parameterValues = null;
            try {
                parameterValues = this.parseDoubleValues(row.get(i), row.get(i + 1));
            } catch (Exception e) {
            }
            if (parameterValues == null) {
                parameterValues = new ArrayList<>();
                parameterValues.add(0.0);
                parameterValues.add(0.0);
            }
            session.addOneDoneExercise(exercise.getName(), parameterValues);
        }
        return session;
    }
    
    private List<Double> parseDoubleValues(Object object1, Object object2) {
        double parameterValue1;
        double parameterValue2;
        try {
            parameterValue1 = Double.parseDouble(object1.toString());
            parameterValue2 = Double.parseDouble(object2.toString());
        } catch (Exception e) {
            return null;
        }
        List<Double> parameterValues = new ArrayList<>();
        parameterValues.add(parameterValue1);
        parameterValues.add(parameterValue2);
        return parameterValues;
    }
    
    public List<List<Object>> getSheetValues(String range, String spreadsheetId) throws Exception {
        ValueRange response = this.sheetsService.spreadsheets().values().get(spreadsheetId, range).setMajorDimension("ROWS").execute();
        List<List<Object>> rows = response.getValues();
        
        if (rows != null && rows.size() != 0) {
            return rows;
        } else {
            return null;
        }
    }

    public Date parseStringToDate(String dateAsString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(dateAsString);
        } catch (Exception e) {
            return null;
        }
        return date;
    }
    
    public String parseDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateAsString = dateFormat.format(date);
        return dateAsString;
    }
}
