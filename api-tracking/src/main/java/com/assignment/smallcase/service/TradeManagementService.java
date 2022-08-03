package com.assignment.smallcase.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.smallcase.exception.NoTradeFoundException;
import com.assignment.smallcase.exception.NotEnoughQuantityToSellException;
import com.assignment.smallcase.exception.NothingToUpdateException;
import com.assignment.smallcase.exception.PortfolioTrackingApiException;
import com.assignment.smallcase.exception.TradeRemovalException;
import com.assignment.smallcase.model.Holding;
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
	private TradeService tradeService;
	@Autowired
	private ExternalService extService;
	public static final String USER_ID = "e82b2e4a-942c-493e-9fb6-5ba3eceef145";

	public ResponseEntity<AddTradeResponse> addTrade(String tradeId, AddTradeRequest addTradeRequest) {
		AddTradeResponse addTradeResponse = new AddTradeResponse();
		Trade trade = createTradeModelFromRequest(addTradeRequest);
		UserPortfolio result=null;
		try {
			if (tradeId != null) {
				trade.setId(tradeId);
				result=updateTrade(tradeId, trade);
				addTradeResponse.setMessage("Trade Updated Successfully");				
			} else {
				Optional<UserPortfolio> userPortfolioDoc = portfolioRepo.findById(USER_ID);
				result=executeTrade(trade,userPortfolioDoc);
				addTradeResponse.setMessage("Trade Added Successfully");
			}
			if(result!=null) {
				updateTotalInvestment(result);
				portfolioRepo.save(result);
				tradeRepository.save(trade);
			}
		
		} catch (final PortfolioTrackingApiException folioTrackingApi) {
			Throwable temp = ExceptionUtils.getCause(folioTrackingApi);
			if (temp instanceof NotEnoughQuantityToSellException)
				throw new NotEnoughQuantityToSellException();
		}
		return ResponseEntity.ok().body(addTradeResponse);

	}
	
	public ResponseEntity<String> removeTrade(String tradeId){
		Trade trade=tradeService.getTradeById(tradeId).getBody();
		Optional<UserPortfolio> userPortfolio=removeTradeFromUserStockHolding(trade);
		if(!userPortfolio.isEmpty()) {
			portfolioRepo.save(userPortfolio.get());
			return ResponseEntity.accepted().body("Trade has been removed successfully");
		}else
			throw new TradeRemovalException();
	}

	private UserPortfolio executeTrade(Trade trade,Optional<UserPortfolio> userPortfolioDoc) throws PortfolioTrackingApiException {
		UserPortfolio userPortfolio = null;
		if (!userPortfolioDoc.isEmpty()) {
			userPortfolio = userPortfolioDoc.get();
		}
		if (trade.getType().equals(TradeType.BUY)) {
			if (userPortfolio != null) {
				Holding userStockHolding = userPortfolio.getHoldings().get(trade.getStock().getId());
				if (userStockHolding != null) {
					int updatedQty = userStockHolding.getQty() + trade.getQty();
					userStockHolding.setAvgBuy((userStockHolding.getAvgBuy() * userStockHolding.getQty()
							+ trade.getQty() * trade.getPrice()) / updatedQty);
					userStockHolding.setQty(updatedQty);

				} else {
					userStockHolding = createNewStockHolding(trade);
				}
				userPortfolio.getHoldings().put(trade.getStock().getId(), userStockHolding);
				userPortfolio.setCurrentValue(userPortfolio.getCurrentValue()
						+ trade.getQty() * extService.getCurrentStockPrice(trade.getStock()));
				userPortfolio
						.setTotalInvestment(userPortfolio.getTotalInvestment() + trade.getQty() * trade.getPrice());

			} else {
				// create user portfolio
				userPortfolio = new UserPortfolio();
				userPortfolio.setId(USER_ID);
				Map<String, Holding> holdings = new HashMap<>();
				Holding holding = createNewStockHolding(trade);
				holdings.put(trade.getStock().getId(), holding);
				userPortfolio.setHoldings(holdings);
				userPortfolio.setTotalInvestment(holding.getQty() * trade.getPrice());
				userPortfolio.setCurrentValue(holding.getQty() * extService.getCurrentStockPrice(trade.getStock()));
			}
			// save userportfolio
			//portfolioRepo.save(userPortfolio);
			return userPortfolio;
		} else {

			// if SELL Trade
			if (userPortfolio != null) {
				Holding userStockHolding = userPortfolio.getHoldings().get(trade.getStock().getId());
				if (userStockHolding != null) {
					// check if sufficient quantity available to sell
					if (userStockHolding.getQty() >= trade.getQty()) {
						userStockHolding.setQty(userStockHolding.getQty() - trade.getQty());
						if((userStockHolding.getQty() - trade.getQty())==0)
							userStockHolding.setAvgBuy(0.00);
						userPortfolio.setTotalInvestment(
								userPortfolio.getTotalInvestment() - (trade.getQty() * trade.getPrice()));
						userPortfolio.setCurrentValue(userPortfolio.getCurrentValue()
								- (trade.getQty() * extService.getCurrentStockPrice(trade.getStock())));
						userPortfolio.getHoldings().put(trade.getStock().getId(), userStockHolding);
					//	portfolioRepo.save(userPortfolio);
						return userPortfolio;
					} else {
						// Throw Not enough quantity to sell
						throw new NotEnoughQuantityToSellException();
					}
				} else {
					// Throw Exceptioo Stock Holding Doesn't Exist
					throw new NotEnoughQuantityToSellException();

				}
			} else {
				// THROW Exception Portfolio Doesn't Exist
				throw new NotEnoughQuantityToSellException();

			}

		}

	}

	private UserPortfolio updateTrade(String tradeId, Trade userUpdatedTradeModel) throws PortfolioTrackingApiException {
		Optional<Trade> oldTradeModel = tradeRepository.findById(tradeId);
		if (oldTradeModel.isEmpty()) {
			throw new NoTradeFoundException();
		} else {
			String oldStockId = oldTradeModel.get().getStock().getId();
			String userUpdatedStockId = userUpdatedTradeModel.getStock().getId();
			Optional<UserPortfolio> userPortfolioDoc = portfolioRepo.findById(USER_ID);
			Holding userStockHolding = userPortfolioDoc.get().getHoldings().get(oldStockId);
			// if Stock is changed OR (if same and type is different) complete quantity
			// removal will happen
			if (!oldStockId.equals(userUpdatedStockId) || (oldStockId.equals(userUpdatedStockId)
					&& !oldTradeModel.get().getType().equals(userUpdatedTradeModel.getType()))) {
				if (oldTradeModel.get().getType().equals(TradeType.BUY)
						&& (userStockHolding.getQty() - oldTradeModel.get().getQty()) < 0) {
					throw new NotEnoughQuantityToSellException();
				}
				//remove the old trade
				Optional<UserPortfolio> updatedUserPortfolioDoc=removeTradeFromUserStockHolding(oldTradeModel.get());
				//now execute the fresh trade
				return executeTrade(userUpdatedTradeModel,updatedUserPortfolioDoc);
			    //update trade details
			//	tradeRepository.save(userUpdatedTradeModel);
			}
			if((oldTradeModel.get().getQty()-userUpdatedTradeModel.getQty())==0 && Double.compare(oldTradeModel.get().getPrice(),userUpdatedTradeModel.getPrice())==0) {
				throw new NothingToUpdateException();
			}
			int delta=userUpdatedTradeModel.getQty()-oldTradeModel.get().getQty();
			if(userUpdatedTradeModel.getType().equals(TradeType.BUY)) {
				if(delta<0 && userStockHolding.getQty()-Math.abs(delta)<0) {
					throw new NotEnoughQuantityToSellException();
				}
				
				if(userStockHolding.getQty()-oldTradeModel.get().getQty()<=0) {
					userStockHolding.setAvgBuy(0.0);
				}else
					userStockHolding.setAvgBuy((userStockHolding.getAvgBuy()*userStockHolding.getQty()-oldTradeModel.get().getPrice()*oldTradeModel.get().getQty())/(userStockHolding.getQty()-oldTradeModel.get().getQty()));
				userStockHolding.setAvgBuy((userStockHolding.getAvgBuy()* (userStockHolding.getQty()-oldTradeModel.get().getQty()) + userUpdatedTradeModel.getPrice()*userUpdatedTradeModel.getQty())/(userStockHolding.getQty()-oldTradeModel.get().getQty() + userUpdatedTradeModel.getQty()));
				if(delta==0) {
					 userPortfolioDoc.get().getHoldings().put(oldTradeModel.get().getStock().getId(), userStockHolding);
					 //save userPortfolioDoc
					 //portfolioRepo.save(userPortfolioDoc.get());
					 return userPortfolioDoc.get();
				}else if(delta>0) {
					userStockHolding.setQty(userStockHolding.getQty()+delta);
					userPortfolioDoc.get().getHoldings().put(oldTradeModel.get().getStock().getId(), userStockHolding);
					 //save userPortfolioDoc
					 //portfolioRepo.save(userPortfolioDoc.get());
					return userPortfolioDoc.get();
				}else {
					userStockHolding.setQty(userStockHolding.getQty()-Math.abs(delta));
					userPortfolioDoc.get().getHoldings().put(oldTradeModel.get().getStock().getId(), userStockHolding);
					 //save userPortfolioDoc
					// portfolioRepo.save(userPortfolioDoc.get());
					return userPortfolioDoc.get();
				}
			}else {
				if(delta>0 && userStockHolding.getQty()-delta<0)
					throw new NotEnoughQuantityToSellException();
				if(delta>0) {
					userStockHolding.setQty(userStockHolding.getQty()-delta);
					userPortfolioDoc.get().getHoldings().put(oldTradeModel.get().getStock().getId(), userStockHolding);
					 //save userPortfolioDoc
					 //portfolioRepo.save(userPortfolioDoc.get());
					return userPortfolioDoc.get();
				}else {
					if(userStockHolding.getQty()==0) {
						throw new NotEnoughQuantityToSellException();
					}
					userStockHolding.setQty(userStockHolding.getQty()+Math.abs(delta));
					userPortfolioDoc.get().getHoldings().put(oldTradeModel.get().getStock().getId(), userStockHolding);
					 //save userPortfolioDoc
					 //portfolioRepo.save(userPortfolioDoc.get());
					return userPortfolioDoc.get();
				}
			}
			
		}
		//tradeRepository.save(userUpdatedTradeModel); 
	}
	
	private UserPortfolio updateTotalInvestment(UserPortfolio userPortfolio) {
		Double totalInvestment=0.0;
		Double currentInvestment=0.00;
		for(Holding holding:userPortfolio.getHoldings().values()) {
			totalInvestment+=holding.getAvgBuy()*holding.getQty();
			currentInvestment+=holding.getQty()*100;
		}
		userPortfolio.setCurrentValue(currentInvestment);
		userPortfolio.setTotalInvestment(totalInvestment);
		return userPortfolio;
	}
	

	private Holding createNewStockHolding(Trade trade) {
		Holding userStockHolding = new Holding();
		userStockHolding.setQty(trade.getQty());
		userStockHolding.setTicker(trade.getStock().getTicker());
		userStockHolding.setAvgBuy(trade.getPrice());
		return userStockHolding;
	}

	private Trade createTradeModelFromRequest(AddTradeRequest addTradeRequest) {
		Trade trade = new Trade();
		trade.setId(UUID.randomUUID().toString());
		trade.setPrice(addTradeRequest.getPrice());
		trade.setQty(addTradeRequest.getQty());
		trade.setStock(addTradeRequest.getStock());
		trade.setType(addTradeRequest.getTradeType());
		trade.setUserId(USER_ID);
		return trade;
	}

	private Optional<UserPortfolio> removeTradeFromUserStockHolding(Trade trade) throws PortfolioTrackingApiException {
		Optional<UserPortfolio> userPortfolioDoc = portfolioRepo.findById(USER_ID);
		Holding userStockHolding = userPortfolioDoc.get().getHoldings().get(trade.getStock().getId());
		if (trade.getType().equals(TradeType.BUY)) {
			if ((userStockHolding.getQty() - trade.getQty()) >= 0) {
				// check if not zero
				if ((userStockHolding.getQty() - trade.getQty()) != 0) {
					userStockHolding.setAvgBuy((userStockHolding.getAvgBuy() * userStockHolding.getQty()
							- trade.getPrice() * trade.getQty()) / (userStockHolding.getQty() - trade.getQty()));
					
					userStockHolding.setQty(userStockHolding.getQty()-trade.getQty());
					userPortfolioDoc.get().getHoldings().put(trade.getStock().getId(), userStockHolding);
				}else {
					userStockHolding.setAvgBuy(0.0);
					userStockHolding.setQty(0);
					userPortfolioDoc.get().getHoldings().put(trade.getStock().getId(), userStockHolding);
				}

			} else {
				throw new TradeRemovalException();
			}
		}else {
			userStockHolding.setQty(userStockHolding.getQty()+trade.getQty());
			userPortfolioDoc.get().getHoldings().put(trade.getStock().getId(), userStockHolding);
		}
		//save updated doc
	//	portfolioRepo.save(userPortfolioDoc.get());
		return userPortfolioDoc;
	}
}
