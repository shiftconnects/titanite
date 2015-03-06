package shift.titanite;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jeff on 3/4/15.
 */
public class EventReceiver {
  @Autowired ErrorPublisher errorPublisher;
  @Autowired EventRepository eventRepository;

  public void receiveMessage(Event message){

    //Save to db if data is valid
    if(message.isValid()){
      try {
        if(!eventRepository.saveEvent(message)) {
          errorPublisher.sendErrorEvent(message, "Event updated 0 rows");
        }
      }
      catch (Exception e){
        errorPublisher.sendErrorEvent(message, e.getClass() + ":" + e.getMessage());
      }
    }
    //Send message to dead queue with header
    else{
      errorPublisher.sendErrorEvent(message, "Event Data Invalid");
    }

    System.out.println("Received <" + message + ">");
  }
}
