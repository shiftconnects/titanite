package shift.titanite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests that the error publisher
 */
public class ErrorPublisherTest {

  @Mock RabbitMessagingTemplate rabbitMessagingTemplate;

  private ErrorPublisher errorPublisher;


  @Before
  public void setupMockWrapper() {
    MockitoAnnotations.initMocks(this);
    errorPublisher = new ErrorPublisher(rabbitMessagingTemplate, "exchange", "route");
  }

  @Test
  public void sendErrorEvent_OriginalEvent_SendErrorMessage(){
    Event event = new Event();
    Map<String, Object> headers = new HashMap<String, Object>();
    headers.put("foo", "bar");
    Message<Event> message = new GenericMessage<Event>(event, headers);

    //Mock the messaging template
    doNothing().when(rabbitMessagingTemplate).send(anyString(), anyString(), isA(Message.class));

    errorPublisher.sendErrorEvent(message, "Test Error");

    //Capture the message to make sure that data is correct
    ArgumentCaptor<Message> argument = ArgumentCaptor.forClass(Message.class);
    verify(rabbitMessagingTemplate, times(1)).send(anyString(), anyString(), argument.capture());

    Message<Event> errorMessage = argument.getValue();
    assertTrue("Original header missing", errorMessage.getHeaders().containsKey("foo"));
    assertEquals("Orignal header changed", errorMessage.getHeaders().get("foo"), "bar");
    assertTrue("Error header missing", errorMessage.getHeaders().containsKey("X-ERROR"));
    assertEquals("Error header incorrect", errorMessage.getHeaders().get("X-ERROR"), "Test Error");
    assertEquals("Event payload changed", event, errorMessage.getPayload());
  }
}
