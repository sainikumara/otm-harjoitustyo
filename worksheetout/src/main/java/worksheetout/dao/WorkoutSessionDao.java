package worksheetout.dao;

import java.util.List;
import worksheetout.domain.WorkoutSession;

public interface WorkoutSessionDao {

    WorkoutSession create(WorkoutSession workoutSession) throws Exception;

    List<WorkoutSession> getAll();

    void setDone(int id) throws Exception;

}
