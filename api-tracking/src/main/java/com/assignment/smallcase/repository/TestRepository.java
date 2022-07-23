package com.assignment.smallcase.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assignment.smallcase.model.Stock;
import com.assignment.smallcase.model.TestModel;
import com.assignment.smallcase.model.User;

public interface TestRepository extends MongoRepository<User,String>{
		
}
