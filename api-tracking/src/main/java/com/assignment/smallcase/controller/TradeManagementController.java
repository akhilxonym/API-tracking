package com.assignment.smallcase.controller;

import java.lang.invoke.MethodHandles;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.smallcase.request.AddTradeRequest;
import com.assignment.smallcase.response.AddTradeResponse;
import com.assignment.smallcase.service.TradeManagementService;
import com.assignment.smallcase.utility.QtyAndPriceValidator;

@RestController
@RequestMapping(value="/stock/trade")
public class TradeManagementController {
	@Autowired
	private TradeManagementService tradeManagementService;
	
	private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@PostMapping("/order")
	public ResponseEntity<AddTradeResponse> addTrade(@RequestParam(required = false) String tradeId,@RequestBody @Valid AddTradeRequest addTradeRequest){
		logger.info("calling add trade method");
		return tradeManagementService.addTrade(tradeId,addTradeRequest);
	}
	
	@DeleteMapping
	@RequestMapping("/remove")
	public ResponseEntity<String> removeTrade(@RequestParam(required = true) String tradeId){
		logger.info("calling remove trade method for"+tradeId);
		return tradeManagementService.removeTrade(tradeId);
	}
}
