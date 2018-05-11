/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worksheetout.dao;

import java.util.Date;
import java.util.List;
import worksheetout.domain.Routine;
import worksheetout.domain.WorkoutSession;

/**
 *
 * @author skumara
 */
public interface WorkoutSessionDao {

    List<WorkoutSession> getWorkoutSessions(Routine routine, String spreadsheetId) throws Exception;

    String parseDateToString(Date date);

    Date parseStringToDate(String dateAsString);

    /**
     * Save a workout session to a Google Sheets document
     * @param session the workout session to be saved
     * @param spreadsheetId the id of the sheets document in which the session is to be saved
     * @throws Exception
     */
    void save(WorkoutSession session, String spreadsheetId) throws Exception;
    
}
