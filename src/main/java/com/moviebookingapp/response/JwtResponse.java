package com.moviebookingapp.response;

import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	
	private String token;
	private String type = "Bearer";
	private ObjectId _id;
	private String loginId;
	private String email;
	private List<String> roles;
	
	public JwtResponse(String token, ObjectId _id, String loginId, String email, List<String> roles) {
		super();
		this.token = token;
		this._id = _id;
		this.loginId = loginId;
		this.email = email;
		this.roles = roles;
	}
	
	

}
