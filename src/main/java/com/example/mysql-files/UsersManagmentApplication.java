package com.example.mysqlfiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class UsersManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersManagmentApplication.class, args);
	}

}
