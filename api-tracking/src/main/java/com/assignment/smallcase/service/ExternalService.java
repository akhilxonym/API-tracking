package com.assignment.smallcase.service;

import java.math.BigDecimal;

import com.assignment.smallcase.model.Stock;

public class ExternalService {
	public BigDecimal getCurrentStockPrice(Stock stock) {
		return new BigDecimal("100.0");
	}
}
