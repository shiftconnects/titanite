package shift.titanite;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Created by jeff on 3/4/15.
 */
public class ErrorPublisher {

  private RabbitTemplate rabbitTemplate;
  private String exchange;
  private String routingKey;

  public ErrorPublisher(RabbitTemplate rabbitTemplate, String exchange, String routingKey){
    this.rabbitTemplate = rabbitTemplate;
    this.exchange = exchange;
    this.routingKey = routingKey;
  }

  public void sendErrorEvent(Event event, String error){
    rabbitTemplate.convertAndSend(exchange, routingKey, event, new ErrorMessagePostProcessor(error));
  }

  /**
   * Simple post processor class to tack on an error header before it goes out
   */
  class ErrorMessagePostProcessor implements MessagePostProcessor {

    private String errorMessage;

    public ErrorMessagePostProcessor(String errorMessage){
      super();
      this.errorMessage = errorMessage;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
      message.getMessageProperties().setHeader("X-ERROR", errorMessage);
      return message;
    }

  }
}
