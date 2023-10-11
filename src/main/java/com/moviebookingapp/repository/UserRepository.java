package com.moviebookingapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moviebookingapp.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	Optional<User> findByLoginId(String username);
	
	Boolean existsByEmail(String email);
	Boolean existsByLoginId(String loginId);
	

}
