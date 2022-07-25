package com.assignment.smallcase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.smallcase.exception.NoTradeFoundException;
import com.assignment.smallcase.model.Trade;
import com.assignment.smallcase.repository.TradeRepository;

@Service
public class TradeService {
	@Autowired
	private TradeRepository tradeRepository;
	
	public ResponseEntity<List<Trade>> getAllTrades(){
		return ResponseEntity.ok(tradeRepository.findAll());
	}
	
	public ResponseEntity<Trade> getTradeById(String tradeId){
		Optional<Trade> trade=tradeRepository.findById(tradeId);
		if(trade.isEmpty())
			throw new NoTradeFoundException();
		return ResponseEntity.ok(trade.get());
	}
}
