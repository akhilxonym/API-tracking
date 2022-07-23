package com.assignment.smallcase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assignment.smallcase.model.Stock;

public interface  StockRepository extends MongoRepository<Stock,String>{

}
