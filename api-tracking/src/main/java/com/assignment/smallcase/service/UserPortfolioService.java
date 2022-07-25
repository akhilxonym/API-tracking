package com.assignment.smallcase.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.smallcase.model.UserPortfolio;
import com.assignment.smallcase.repository.PortfolioRepository;

@Service
public class UserPortfolioService {
	@Autowired
	private PortfolioRepository portfolioRepository;
	
	public static final String USER_ID = "e82b2e4a-942c-493e-9fb6-5ba3eceef145";

	
	public ResponseEntity<UserPortfolio> getUserPortfolio(){
		Optional<UserPortfolio> portfolio=portfolioRepository.findById(USER_ID);
		if(portfolio.isEmpty())
			return ResponseEntity.ok().body(null);
		return ResponseEntity.ok().body(portfolio.get());
	}
}
