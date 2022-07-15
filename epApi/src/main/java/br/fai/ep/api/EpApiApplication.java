package br.fai.ep.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.fai.ep.*"})
public class EpApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EpApiApplication.class, args);
    }

}
