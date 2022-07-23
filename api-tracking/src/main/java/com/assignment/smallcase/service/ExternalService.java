package com.assignment.smallcase.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.assignment.smallcase.model.Stock;
@Service
public class ExternalService {
	public Double getCurrentStockPrice(Stock stock) {
		return 100.0;
	}
}
