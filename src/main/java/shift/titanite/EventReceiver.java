package shift.titanite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/**
 * Core receiver.  Takes messages and saves them to a database.  Otherwise requeues them with
 * the error message to explain what happened
 */
public class EventReceiver {
  @Autowired ErrorPublisher errorPublisher;
  @Autowired EventRepository eventRepository;

  private static final Logger LOG = LoggerFactory.getLogger(EventReceiver.class);

  @RabbitListener(containerFactory = "rabbitListenerContainerFactory", queues = "${rabbit.event.queue}")
  public void receiveMessage(Message<Event> message){

    Event payload = message.getPayload();

    //Save to db if data is valid
    if(payload.isValid()){
      try {
        if(!eventRepository.saveEvent(payload)) {
          errorPublisher.sendErrorEvent(message, "Event updated 0 rows");
          LOG.info("Event save updated nothing: {}", payload);
        }
      }
      catch (Exception e){
        errorPublisher.sendErrorEvent(message, e.getClass() + ":" + e.getMessage());
        LOG.error("Event save failed: {}", e);
      }
    }
    //Send payload to dead queue with header
    else{
      errorPublisher.sendErrorEvent(message, "Event Data Invalid");
      LOG.info("Received invalid event data: {}", payload);
    }

    System.out.println("Received <" + payload + ">");
  }
}
