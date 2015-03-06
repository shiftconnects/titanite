package shift.titanite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TitaniteApplication {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(TitaniteApplication.class, args);
  }

}
