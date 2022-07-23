package com.assignment.smallcase.request;

import com.assignment.smallcase.model.Stock;
import com.assignment.smallcase.model.TradeType;
import com.assignment.smallcase.utility.QtyAndPriceValidator;
@QtyAndPriceValidator
public class AddTradeRequest {
	private Stock stock;
	private int qty;
	private TradeType tradeType;
	private Double price;
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public TradeType getTradeType() {
		return tradeType;
	}
	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "AddTradeRequest [stock=" + stock + ", qty=" + qty + ", tradeType=" + tradeType + ", price=" + price
				+ "]";
	}
	
}
