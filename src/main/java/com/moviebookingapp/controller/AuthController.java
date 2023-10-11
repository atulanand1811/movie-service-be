package com.moviebookingapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebookingapp.request.LoginRequest;
import com.moviebookingapp.request.RegisterRequest;
import com.moviebookingapp.response.JwtResponse;
import com.moviebookingapp.response.MessageResponse;
import com.moviebookingapp.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
@Slf4j
@CrossOrigin("*")
public class AuthController {

	@Autowired
	AuthService aService;

	@PostMapping("/register")
	@Operation(summary = "new registration")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest register) {

		int registerUserFlag = aService.registerUser(register);
		if (registerUserFlag == -1) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Already registered with the login id"));
		} else if (registerUserFlag == 0) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Already registered with the Email"));
		}

		return ResponseEntity.ok(new MessageResponse("User Registered Successfully !!"));

	}

	@PostMapping("/login")
	@Operation(summary = "login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		JwtResponse authenticateUser = aService.authenticateUser(loginRequest);
		return ResponseEntity.ok(authenticateUser);

	}

	@PutMapping("/{loginId}/forgot")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "reset password")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<String> changePassword(@RequestBody LoginRequest loginRequest, @PathVariable String loginId) {
		log.debug("forgot password endopoint accessed by " + loginRequest.getLoginId());
		String res = aService.changePassword(loginRequest, loginId);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

}
