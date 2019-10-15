package com.example.postgresdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//https://www.callicoder.com/spring-boot-jpa-hibernate-postgresql-restful-crud-api-example/

@SpringBootApplication
@EnableJpaAuditing
public class PostgresDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgresDemoApplication.class, args);
	}

}
