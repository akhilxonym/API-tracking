package com.assignment.smallcase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assignment.smallcase.model.Trade;

public interface TradeRepository extends MongoRepository<Trade,String>{

}
