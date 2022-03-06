package com.learning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.model.AuthenticationRequest;
import com.learning.model.AuthenticationResponse;
import com.learning.service.UserServiceImpl;
import com.learning.utils.JwtTokenUtil;

@RestController
public class RootController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@GetMapping("/")
	public String indexController() {
		return "index";
	}
	
	@PostMapping("/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest request) {
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
		}catch (BadCredentialsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		final UserDetails userdetails=userServiceImpl.loadUserByUsername(request.getUserName());
		
		final String jwt=jwtTokenUtil.generateToken(userdetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	

}
