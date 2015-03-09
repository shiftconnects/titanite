package shift.titanite;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository to store object with json data into postgres
 */
public class EventRepository {

  @Autowired private NamedParameterJdbcTemplate template;
  @Autowired ObjectMapper objectMapper;
  private String tableName;
  private String insertQuery;

  private static final Logger LOG = LoggerFactory.getLogger(EventRepository.class);

  public EventRepository(String tableName){
    this.tableName = tableName;
    this.insertQuery = "INSERT INTO " + tableName
    + " (data, source, occurred, type, tuid) VALUES(:data, :source, :occurred, :type, :tuid)";

  }

  /**
   * Java Postgres has no good way to store the json payload for the events.  This requires
   * a low level prepared statement to force the json type and push the object to the database
   * @param event
   * @return
   * @throws SQLException
   * @throws JsonProcessingException
   */
  public boolean saveEvent(Event event) throws SQLException, JsonProcessingException{
    boolean success = false;

    //Set up a specific PGobject which allows setting of the type as well as data
    PGobject data = new PGobject();
    data.setType("json");
    data.setValue(objectMapper.writeValueAsString(event.getData()));

    Map<String, Object> params = new HashMap<String, Object>();
    params.put("data", data);
    params.put("source", event.getSource());
    params.put("occurred", new Timestamp(event.getOccurred().toEpochMilli()));
    params.put("type", event.getType());
    params.put("tuid", event.getTuid());

    int result = template.update(insertQuery, params);
    if(result > 0){
      success = true;
    }

    return success;
  }

}
