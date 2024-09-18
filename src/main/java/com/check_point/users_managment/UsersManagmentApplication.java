package com.check_point.users_managment;

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
