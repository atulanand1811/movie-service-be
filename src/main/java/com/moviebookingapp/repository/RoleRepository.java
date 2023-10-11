package com.moviebookingapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moviebookingapp.models.ERole;
import com.moviebookingapp.models.Role;

public interface RoleRepository extends MongoRepository<Role, String>{

	Optional<Role> findByName(ERole name);
	
}
