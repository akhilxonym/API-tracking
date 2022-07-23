package com.assignment.smallcase.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.smallcase.model.Holding;
import com.assignment.smallcase.model.Stock;
import com.assignment.smallcase.model.Trade;
import com.assignment.smallcase.model.TradeType;
import com.assignment.smallcase.model.UserPortfolio;
import com.assignment.smallcase.repository.PortfolioRepository;
import com.assignment.smallcase.repository.TradeRepository;
import com.assignment.smallcase.request.AddTradeRequest;
import com.assignment.smallcase.response.AddTradeResponse;

@Service
public class TradeManagementService {
	@Autowired
	private PortfolioRepository portfolioRepo;
	@Autowired
	private TradeRepository tradeRepository;
	
	@Autowired
	private ExternalService extService;
	public ResponseEntity<AddTradeResponse> addTrade(AddTradeRequest addTradeRequest){
		Trade trade=createTradeModelFromRequest(addTradeRequest);
		try {
			executeTrade(trade);
		}catch(Exception e) {
			
		}
		AddTradeResponse addTradeResponse=new AddTradeResponse();
		addTradeResponse.setMessage("Trade Added Successfully");
		tradeRepository.save(trade);
		return ResponseEntity.ok().body(addTradeResponse);
		
	}
	
	private void executeTrade(Trade trade) throws Exception {
		Optional<UserPortfolio> userPortfolioDoc=portfolioRepo.findById("e82b2e4a-942c-493e-9fb6-5ba3eceef145");
		UserPortfolio userPortfolio=null;
		if(!userPortfolioDoc.isEmpty()) {
			userPortfolio=userPortfolioDoc.get();
		}
		if(trade.getType().equals(TradeType.BUY)) {
			if(userPortfolio!=null) {
				Holding userStockHolding=userPortfolio.getHoldings().get(trade.getStock());
				if(userStockHolding!=null) {
					int updatedQty=userStockHolding.getQty()+trade.getQty();
					userStockHolding.setAvgBuy((userStockHolding.getAvgBuy()*userStockHolding.getQty() + trade.getQty()*trade.getPrice())/updatedQty);
					userStockHolding.setQty(updatedQty);

				}else {
					userStockHolding=createNewStockHolding(trade);
				}
				userPortfolio.getHoldings().put(trade.getStock().getId(), userStockHolding);
				userPortfolio.setCurrentValue(userPortfolio.getCurrentValue()+trade.getQty()*extService.getCurrentStockPrice(trade.getStock()));
				userPortfolio.setTotalInvestment(userPortfolio.getTotalInvestment()+trade.getQty()*trade.getPrice());
				
			}else {
				//create user portfolio
				userPortfolio=new UserPortfolio();
				userPortfolio.setId("e82b2e4a-942c-493e-9fb6-5ba3eceef145");
				Map<String,Holding> holdings=new HashMap<>();
				Holding holding=createNewStockHolding(trade);
				holdings.put(trade.getStock().getId(), holding);
				userPortfolio.setHoldings(holdings);
				userPortfolio.setTotalInvestment(holding.getQty()*trade.getPrice());
				userPortfolio.setCurrentValue(holding.getQty()*extService.getCurrentStockPrice(trade.getStock()));
			}
			//save userportfolio
			portfolioRepo.save(userPortfolio);
		}else {
			
			// if SELL Trade
			
			if(userPortfolio!=null) {
				Holding userStockHolding=userPortfolio.getHoldings().get(trade.getStock());
				if(userStockHolding!=null) {
					// check if sufficient quantity available to sell
					if(userStockHolding.getQty()>=trade.getQty()) {
						userStockHolding.setQty(userStockHolding.getQty()-trade.getQty());
						userPortfolio.setTotalInvestment(userPortfolio.getTotalInvestment()-(trade.getQty()*trade.getPrice()));
						userPortfolio.setCurrentValue(userPortfolio.getCurrentValue()-(trade.getQty()*extService.getCurrentStockPrice(trade.getStock())));
						userPortfolio.getHoldings().put(trade.getStock().getId(), userStockHolding);
						portfolioRepo.save(userPortfolio);
					}
					else {
						//Throw Not enough quantity to sell
					}
				}else {
					// Throw Exceptio Stock Holding Doesn't Exist
				}
				userPortfolio.getHoldings().put(trade.getStock().getId(), userStockHolding);
			}else {
				// THROW Exception Portfolio Doesn't Exist
			}
			
		}
		
	}
	
	private Holding createNewStockHolding(Trade trade) {
		Holding userStockHolding=new Holding();
		userStockHolding.setQty(trade.getQty());
		userStockHolding.setTicker(trade.getStock().getTicker());
		userStockHolding.setAvgBuy(trade.getPrice());
		return userStockHolding;
	}
	
	private Trade createTradeModelFromRequest(AddTradeRequest addTradeRequest) {
		Trade trade=new Trade();
		trade.setId(UUID.randomUUID().toString());
		trade.setPrice(addTradeRequest.getPrice());
		trade.setQty(addTradeRequest.getQty());
		trade.setStock(addTradeRequest.getStock());
		trade.setType(addTradeRequest.getTradeType());
		return trade;
	}
}
