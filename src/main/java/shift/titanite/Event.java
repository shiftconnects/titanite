package shift.titanite;

import java.time.Instant;
import java.util.Map;

/**
 * Created by jeff on 3/4/15.
 */
public class Event {

  private Map<String, String> data;
  private String source;
  private Instant occurred;
  private String tuid;
  private String type;

  public Map<String, String> getData() {
    return data;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Instant getOccurred() {
    return occurred;
  }

  public void setOccurred(Instant occurred) {
    this.occurred = occurred;
  }

  public String getTuid() {
    return tuid;
  }

  public void setTuid(String tuid) {
    this.tuid = tuid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isValid(){
    return source != null && occurred != null && tuid != null && type != null
        && data != null && !data.isEmpty();
  }

  @Override
  public String toString() {
    return "Event{" +
           "data=" + data +
           ", source='" + source + '\'' +
           ", occurred=" + occurred +
           ", tuid='" + tuid + '\'' +
           ", type='" + type + '\'' +
           '}';
  }
}
