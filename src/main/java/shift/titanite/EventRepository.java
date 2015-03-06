package shift.titanite;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by jeff on 3/5/15.
 */
public class EventRepository {

  @Autowired DataSource dataSource;
  @Autowired ObjectMapper objectMapper;
  private String tableName;

  public EventRepository(String tableName){
    this.tableName = tableName;
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

    PGobject data = new PGobject();
    data.setType("json");

    data.setValue(objectMapper.writeValueAsString(event.getData()));

    String sql = "INSERT INTO " + tableName
                 + " (data, source, occurred, type, tuid) VALUES(?, ?, ?, ?, ?)";

    PreparedStatement statement = dataSource.getConnection().prepareStatement(sql);
    statement.setObject(1, data);
    statement.setString(2, event.getSource());
    statement.setTimestamp(3, new Timestamp(event.getOccurred().toEpochMilli()));
    statement.setString(4, event.getType());
    statement.setString(5, event.getTuid());
    int result = statement.executeUpdate();
    if(result > 0){
      success = true;
    }

    statement.close();
    return success;
  }

}
