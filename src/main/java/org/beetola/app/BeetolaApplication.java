package org.beetola.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class BeetolaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeetolaApplication.class, args);
    }

}
