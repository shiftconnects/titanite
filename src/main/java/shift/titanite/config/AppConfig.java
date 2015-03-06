package shift.titanite.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.time.Instant;
import java.util.HashMap;

import shift.titanite.ErrorPublisher;
import shift.titanite.Event;
import shift.titanite.EventReceiver;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;

@Configuration
public class AppConfig{

  @Value("${rabbit.event.queue}") private String eventQueueName;
  @Value("${rabbit.event.exchange}") private String eventExchangeName;
  @Value("${rabbit.event.route}") private String eventRoute;
  @Value("${rabbit.event.error.exchange}") private String errorEventExchangeName;
  @Value("${rabbit.event.error.route}") private String errorEventRoute;

  @Bean
  public ObjectMapper jacksonObjectMapper(){
    return new ObjectMapper()
        .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
        .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)

        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        .setPropertyNamingStrategy(CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
        .findAndRegisterModules();
  }

  @Bean
  public Jackson2JsonMessageConverter jsonMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

    converter.setJsonObjectMapper(jacksonObjectMapper());

    return converter;
  }

  @Bean
  RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate();

    template.setConnectionFactory(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    template.setExchange(eventExchangeName);

    return template;
  }


  @Bean
  Queue queue(){
    return new Queue(eventQueueName, true, false, false);
  }

  @Bean
  TopicExchange exchange(){
    return new TopicExchange(eventExchangeName);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange topicExchange){
    return BindingBuilder.bind(queue).to(topicExchange).with(eventRoute);
  }

  @Bean
  EventReceiver eventReceiver(){
    return new EventReceiver();
  }

  @Bean
  ErrorPublisher errorPublisher(RabbitTemplate rabbitTemplate){
    return new ErrorPublisher(rabbitTemplate, errorEventExchangeName, errorEventRoute);
  }

  @Bean
  String blah(RabbitTemplate rabbitTemplate, JdbcTemplate jdbcTemplate) {
    Event foo = new Event();
    foo.setData(new HashMap<String, String>());
    foo.getData().put("test", "hello");
    foo.setTuid("123-123-123-123-jifodsaf");
    foo.setSource("phone app");
    foo.setOccurred(Instant.now());
    foo.setType("shift.event");

    rabbitTemplate.convertAndSend(eventExchangeName, eventRoute, foo);

    return "HELLO";
  }

  @Bean
  RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate){
    RabbitMessagingTemplate messagingTemplate = new RabbitMessagingTemplate(rabbitTemplate);
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setObjectMapper(jacksonObjectMapper());
    messagingTemplate.setMessageConverter(converter);

    return messagingTemplate;
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory){
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
    MessageListenerAdapter adapter = new MessageListenerAdapter(eventReceiver(), "receiveMessage");
    adapter.setMessageConverter(jsonMessageConverter());
    container.setMessageListener(adapter);
    container.setPrefetchCount(100);
    container.setQueues(queue());

    return container;
  }
}
