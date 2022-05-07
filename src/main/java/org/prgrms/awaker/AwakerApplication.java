package org.prgrms.awaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AwakerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AwakerApplication.class);
        springApplication.setAdditionalProfiles("db");
        springApplication.run(args);
    }

}
