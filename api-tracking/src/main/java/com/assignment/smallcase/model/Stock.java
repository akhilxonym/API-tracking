package com.assignment.smallcase.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="stock")
public class Stock {
	
	@Id
	private String id;
	private String name;
	private String ticker;
	private Date listingDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public Date getListingDate() {
		return listingDate;
	}
	public void setListingDate(Date listingDate) {
		this.listingDate = listingDate;
	}
	@Override
	public String toString() {
		return "Stock [id=" + id + ", name=" + name + ", ticker=" + ticker + ", listingDate=" + listingDate + "]";
	}
	

}
