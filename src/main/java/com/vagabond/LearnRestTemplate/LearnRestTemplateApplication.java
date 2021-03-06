package com.vagabond.LearnRestTemplate;

import com.vagabond.LearnRestTemplate.model.User;
import com.vagabond.LearnRestTemplate.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LearnRestTemplateApplication implements CommandLineRunner {

	private RestService restService;

	@Autowired
	public LearnRestTemplateApplication(RestService restService) {
		this.restService = restService;
	}


	public static void main(String[] args) {
		SpringApplication.run(LearnRestTemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// get all users
		restService.getUsersWithCookies();

		// create user
		restService.createUser();

		// update user
		restService.updateUser();

		// delete user
		restService.deleteUser();
	}
}
