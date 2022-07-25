package com.assignment.smallcase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	
	@RequestMapping("/")
	public ResponseEntity<?> welcome(){
		return ResponseEntity.ok().body("Welcome Adarsh!");
	}
	
}
