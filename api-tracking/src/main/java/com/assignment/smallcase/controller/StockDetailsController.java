package com.assignment.smallcase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.smallcase.model.Stock;
import com.assignment.smallcase.repository.StockRepository;
import com.assignment.smallcase.service.StockService;

@RestController
@RequestMapping(value="/stock")
public class StockDetailsController {
	@Autowired
	private StockRepository stockRepository;
	
	@Autowired
	private StockService stockService;
	@GetMapping(value="/all")
	ResponseEntity<List<Stock>> getTrades(){
		return stockService.getAllStocks();
	}
	@GetMapping()
	ResponseEntity<Stock> getTradeById(@RequestParam(required = true) String stockId){
		return stockService.getStockById(stockId);
	}
}
