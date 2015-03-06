package shift.titanite;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Created by jeff on 3/6/15.
 */
public class ErrorPublisherTest {

  @Mock RabbitTemplate rabbitTemplate;

  private ErrorPublisher errorPublisher = new ErrorPublisher(rabbitTemplate, "exchange", "route");

  @Test
  public void sendErrorEvent_ValidEvent_InjectsHeaders(){
    Event event = new Event();

    doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), isA(Event.class),
                                                    isA(ErrorPublisher.ErrorMessagePostProcessor.class));

    errorPublisher.sendErrorEvent(event, "Test Error");

    ArgumentCaptor<ErrorPublisher.ErrorMessagePostProcessor> argument
        = ArgumentCaptor.forClass(ErrorPublisher.ErrorMessagePostProcessor.class);
    verify(rabbitTemplate.convertSendAndReceive(anyString(), anyString(),
                                                isA(Event.class), argument.capture()));




  }
}
