package worksheetout.dao;

import java.util.List;
import worksheetout.domain.Routine;
import worksheetout.domain.WorkoutSession;

public interface WorkoutSessionDao {

    void save(WorkoutSession session, String spreadsheetId) throws Exception;

    List<WorkoutSession> getAll();

}
