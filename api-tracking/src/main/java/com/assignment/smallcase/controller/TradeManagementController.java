package com.assignment.smallcase.controller;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.smallcase.request.AddTradeRequest;
import com.assignment.smallcase.response.AddTradeResponse;
import com.assignment.smallcase.utility.QtyAndPriceValidator;

@RestController
@RequestMapping(value="/stock/order")
public class TradeManagementController {
	
	private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@PostMapping
	public ResponseEntity<AddTradeResponse> addTrade(@QtyAndPriceValidator @RequestBody AddTradeRequest addTradeRequest){
		return null;
	}
}
