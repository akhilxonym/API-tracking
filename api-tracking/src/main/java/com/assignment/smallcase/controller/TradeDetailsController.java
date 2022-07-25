package com.assignment.smallcase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.smallcase.model.Trade;
import com.assignment.smallcase.repository.TradeRepository;
import com.assignment.smallcase.service.TradeService;

@RestController
@RequestMapping(value="/trade")
public class TradeDetailsController {

	@Autowired
	private TradeRepository tradeRepository;
	
	@Autowired
	private TradeService tradeService;
	@GetMapping(value="/all")
	ResponseEntity<List<Trade>> getTrades(){
		return tradeService.getAllTrades();
	}
	@GetMapping()
	ResponseEntity<Trade> getTradeById(@RequestParam(required = true) String tradeId){
		return tradeService.getTradeById(tradeId);
	}
}


