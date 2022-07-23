package com.assignment.smallcase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assignment.smallcase.model.UserPortfolio;

public interface PortfolioRepository extends MongoRepository<UserPortfolio,String>{

}
