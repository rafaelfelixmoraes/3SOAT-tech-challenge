package br.com.tech.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = {"br.com.tech.challenge.redis"})
public class TransactionBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionBffApplication.class, args);
    }

}
