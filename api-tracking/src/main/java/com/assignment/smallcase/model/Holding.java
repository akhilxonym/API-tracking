package com.assignment.smallcase.model;

import java.math.BigDecimal;

public class Holding {
	private int qty;
	private String ticker;
	private Double avgBuy;
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public Double getAvgBuy() {
		return avgBuy;
	}
	public void setAvgBuy(Double avgBuy) {
		this.avgBuy = avgBuy;
	}
	@Override
	public String toString() {
		return "Holding [qty=" + qty + ", ticker=" + ticker + ", avgBuy=" + avgBuy + "]";
	}
	
}
