package edu.ucsb.hopefully_unhackable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class QueryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueryServerApplication.class, args);
    }
}
