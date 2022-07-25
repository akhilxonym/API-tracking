package com.assignment.smallcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.smallcase.model.UserPortfolio;
import com.assignment.smallcase.service.UserPortfolioService;

@RestController
@RequestMapping(value="/portfolio")
public class UserPortfolioController {
	@Autowired
	private UserPortfolioService userPortfolioService;

	@GetMapping()
	ResponseEntity<UserPortfolio> getTrades(){
		return userPortfolioService.getUserPortfolio();
	}
}
