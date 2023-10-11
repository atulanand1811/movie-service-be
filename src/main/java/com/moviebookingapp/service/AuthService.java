package com.moviebookingapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moviebookingapp.models.ERole;
import com.moviebookingapp.models.Role;
import com.moviebookingapp.models.User;
import com.moviebookingapp.repository.RoleRepository;
import com.moviebookingapp.repository.UserRepository;
import com.moviebookingapp.request.LoginRequest;
import com.moviebookingapp.request.RegisterRequest;
import com.moviebookingapp.response.JwtResponse;
import com.moviebookingapp.security.jwt.JwtUtils;
import com.moviebookingapp.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {
	
	@Autowired
	AuthenticationManager authenthicationManager;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	RoleRepository rRepo;
	
	@Autowired
    JwtUtils jwtUtils;

	
	public int registerUser(RegisterRequest register) {
		
		if(uRepo.existsByLoginId(register.getLoginId())) {
			return -1;
		}
		if(uRepo.existsByEmail(register.getEmail())) {
			return 0;
		}
		
		User user = new User(register.getLoginId(),
				register.getFirstName(),
				register.getLastName(),
				register.getEmail(),
				register.getContactNumber(),
                encoder.encode(register.getPassword()));
		
		Set<String> roleSet = register.getRoles();
		Set<Role> roles = new HashSet<>();
		
		String errorMsg = "Error: Role is not found.";
		
		if(roleSet == null) {
			Role userRole = rRepo.findByName(ERole.ROLE_USER)
			.orElseThrow(() -> new RuntimeException(errorMsg));
			roles.add(userRole);
		}
		else {
			roleSet.forEach( role -> {
				switch(role) {
					case "admin":
						Role adminRole = rRepo.findByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException(errorMsg));
						roles.add(adminRole);
						break;
					
					case "guest":
						Role guestRole = rRepo.findByName(ERole.ROLE_GUEST)
						.orElseThrow(() -> new RuntimeException(errorMsg));
						roles.add(guestRole);
						break;
					
					default:
						Role userRole = rRepo.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException(errorMsg));
						roles.add(userRole);
				}
			});
		}
		
		user.setRoles(roles);
		
		uRepo.save(user);
		
		return 1;
		
	}
	
	public JwtResponse authenticateUser(LoginRequest loginRequest) {
		
		Authentication authentication = authenthicationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.get_id(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
	}
	
	public String changePassword(LoginRequest loginRequest, String loginId) {
		User user = uRepo.findByLoginId(loginId).get();
		User newUser = new User(user.getLoginId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getContactNumber(),
                encoder.encode(loginRequest.getPassword()));
		newUser.setId(user.getId());
		newUser.setRoles(user.getRoles());
		uRepo.save(newUser);
		log.debug(loginRequest.getLoginId()+" has password changed successfully");
		return "Users password changed successfully";
		
		
	}

}
