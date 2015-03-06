package shift.titanite.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import shift.titanite.EventRepository;


/**
 * Created by jeff on 3/5/15.
 */
@Configuration
public class DataConfig {

  @Value("${jdbc.driverClassName}") String driverClassName;
  @Value("${jdbc.url}") String url;
  @Value("${jdbc.username}") String username;
  @Value("${jdbc.password}") String password;
  @Value("${jdbc.minPoolSize:1}") Integer minPoolSize;
  @Value("${jdbc.maxPoolSize:1}") Integer maxPoolSize;
  @Value("${jdbc.maxConnectionsAge:360000}") Integer maxConnectionsAge;
  @Value("${jdbc.event.table}") String eventTableName;

  @Autowired JdbcTemplate jdbcTemplate;

  @Bean
  DataSource dataSource(){
    DataSource dataSource = new DataSource();

    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(url);
    dataSource.setInitialSize(minPoolSize);
    dataSource.setMinIdle(minPoolSize);
    dataSource.setMaxIdle(Integer.max(minPoolSize * 2, maxPoolSize - minPoolSize));
    dataSource.setMaxActive(maxPoolSize);
    dataSource.setMaxAge(maxConnectionsAge);

    return dataSource;
  }

  @Bean
  EventRepository eventRepository(){
    return new EventRepository(eventTableName);
  }
}
