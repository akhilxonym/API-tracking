package com.assignment.smallcase.model;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="userPortfolio")
public class UserPortfolio {
	@Id
	private String id;
	private Map<String,Holding> holdings;
	private Double totalInvestment;
	private Double currentValue;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map<String, Holding> getHoldings() {
		return holdings;
	}
	public void setHoldings(Map<String, Holding> holdings) {
		this.holdings = holdings;
	}
	public Double getTotalInvestment() {
		return totalInvestment;
	}
	public void setTotalInvestment(Double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	public Double getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}
	@Override
	public String toString() {
		return "UserPortfolio [id=" + id + ", holdings=" + holdings + ", totalInvestment=" + totalInvestment
				+ ", currentValue=" + currentValue + "]";
	}

	
}
