package worksheetout.dao;

import com.google.api.services.sheets.v4.Sheets;
import java.util.List;
import worksheetout.domain.Routine;

public interface RoutineDao {

    void save(Routine routine, String spreadsheetId) throws Exception;

    List<Routine> getAll();

}
