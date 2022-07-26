package com.assignment.smallcase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.smallcase.exception.NoStockFoundException;
import com.assignment.smallcase.model.Stock;
import com.assignment.smallcase.repository.StockRepository;

@Service
public class StockService {
	@Autowired
	private StockRepository stockRepository;
	
	public ResponseEntity<List<Stock>> getAllStocks(){
		return ResponseEntity.ok(stockRepository.findAll());
	}
	
	public ResponseEntity<Stock> getStockById(String tradeId){
		Optional<Stock> trade=stockRepository.findById(tradeId);
		if(trade.isEmpty())
			throw new NoStockFoundException();
		return ResponseEntity.ok(trade.get());
	}
}
