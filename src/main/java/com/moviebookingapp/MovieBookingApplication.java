package com.moviebookingapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.moviebookingapp.models.ERole;
import com.moviebookingapp.models.Movie;
import com.moviebookingapp.models.Role;
import com.moviebookingapp.repository.MovieRepository;
import com.moviebookingapp.repository.RoleRepository;

@SpringBootApplication
public class MovieBookingApplication implements CommandLineRunner {
	
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(MovieBookingApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("roles");
		mongoTemplate.dropCollection("ticket");
		mongoTemplate.dropCollection("users");
		mongoTemplate.dropCollection("movie");

		Movie movie1 = new Movie("Avatar","Inox",36,"Book ASAP");
	 	Movie movie2 = new Movie("Wakanda","PVR",28,"Book ASAP");
	 	Movie movie3 = new Movie("Pathaan","Natraj",10,"Book ASAP");

	 	movieRepository.saveAll(List.of(movie1,movie2,movie3));

		Role admin = new Role(ERole.ROLE_ADMIN);
		Role user = new Role(ERole.ROLE_USER);

		roleRepository.saveAll(List.of(admin,user));
	}

}
