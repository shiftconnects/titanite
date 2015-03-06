package shift.titanite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;

/**
 * Created by jeff on 3/4/15.
 */
public class ErrorPublisher {

  private static final Logger LOG = LoggerFactory.getLogger(ErrorPublisher.class);

  private RabbitMessagingTemplate rabbitMessagingTemplate;
  private String exchange;
  private String routingKey;

  public ErrorPublisher(RabbitMessagingTemplate rabbitMessagingTemplate, String exchange,
                        String routingKey){
    this.rabbitMessagingTemplate = rabbitMessagingTemplate;
    this.exchange = exchange;
    this.routingKey = routingKey;
  }

  public void sendErrorEvent(Message<Event> message, String error){
    HashMap<String, Object> headers = new HashMap<String, Object>(message.getHeaders());
    headers.put("X-ERROR", error);
    Message<Event> errorMessage = new GenericMessage<Event>(message.getPayload(), headers);

    rabbitMessagingTemplate.send(exchange, routingKey, errorMessage);
  }

}
