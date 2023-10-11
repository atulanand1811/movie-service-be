package com.moviebookingapp.models;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class User {
	

	@Id
	private ObjectId id;
	
	@NotBlank
	@Size(max=20)
	private String loginId;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	private String contactNumber;
	
	@NotBlank
	@Size(max=120)
	private String password;
	
	@DBRef
	private Set<Role> roles = new HashSet<>();
	
	 public User(String loginId, String firstName, String lastName, String email, String contactNumber, String password) {
	        this.loginId = loginId;
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.email = email;
	        this.contactNumber = contactNumber;
	        this.password = password;
	    }

}
