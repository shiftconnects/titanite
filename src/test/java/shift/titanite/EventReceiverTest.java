package shift.titanite;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by jeff on 3/6/15.
 */
public class EventReceiverTest {

  @Mock ErrorPublisher errorPublisher;
  @Mock EventRepository eventRepository;
  @InjectMocks EventReceiver eventReceiver = new EventReceiver();

  @Before
  public void setupMockWrapper() {
    MockitoAnnotations.initMocks(this);

  }

//  @Test
//  public void receiveMessage_InValidMessage_PublishToFailures(){
//    Event invalidEvent = new Event();
//    invalidEvent.setSource("phone");
//    invalidEvent.setData(new HashMap<String, String>());
//
//    //Convert to message
//    Message<Event> message = new GenericMessage<Event>(invalidEvent);
//
//    doNothing().when(errorPublisher).sendErrorEvent(isA(Message.class), anyString());
//
//    //Make it so
//    eventReceiver.receiveMessage(message);
//
//    verify(errorPublisher, times(1)).sendErrorEvent(message, "Event Data Invalid");
//  }
//
//  @Test
//  public void receiveMessage_ValidMessageSqlException_PublishToFailures() throws
//                                                                          JsonProcessingException,
//                                                                          SQLException {
//    Event event = new Event();
//    event.setData(new HashMap<String, String>());
//    event.getData().put("location", "36.12345, -115.4567");
//    event.setTuid("123-123-123-123-jifodsaf");
//    event.setSource("phone app");
//    event.setOccurred(Instant.now());
//    event.setType("shift.event");
//
//    //Convert to message
//    Message<Event> message = new GenericMessage<Event>(event);
//
//    doNothing().when(errorPublisher).sendErrorEvent(isA(Event.class), anyString());
//    when(eventRepository.saveEvent(isA(Event.class)))
//        .thenThrow(new SQLException("Connection Error"));
//
//    //Make it so
//    eventReceiver.receiveMessage(message);
//
//    verify(errorPublisher, times(1)).sendErrorEvent(event,
//                                                    "class java.sql.SQLException:Connection Error");
//  }
//
//  @Test
//  public void receiveMessage_ValidMessageNoInsert_PublishToFailures() throws
//                                                                          JsonProcessingException,
//                                                                          SQLException {
//    Event event = new Event();
//    event.setData(new HashMap<String, String>());
//    event.getData().put("location", "36.12345, -115.4567");
//    event.setTuid("123-123-123-123-jifodsaf");
//    event.setSource("phone app");
//    event.setOccurred(Instant.now());
//    event.setType("shift.event");
//
//    //Convert to message
//    Message<Event> message = new GenericMessage<Event>(event);
//
//    doNothing().when(errorPublisher).sendErrorEvent(isA(Event.class), anyString());
//    when(eventRepository.saveEvent(isA(Event.class))).thenReturn(false);
//
//    //Make it so
//    eventReceiver.receiveMessage(message);
//
//    verify(errorPublisher, times(1)).sendErrorEvent(event, "Event updated 0 rows");
//    verify(eventRepository).saveEvent(event);
//  }
}
