package com.assignment.smallcase.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.smallcase.exception.NoTradeFoundException;
import com.assignment.smallcase.exception.NotEnoughQuantityToSellException;
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
	private ExternalService extService;
	public static final String USER_ID = "e82b2e4a-942c-493e-9fb6-5ba3eceef145";

	public ResponseEntity<AddTradeResponse> addTrade(String tradeId, AddTradeRequest addTradeRequest) {
		AddTradeResponse addTradeResponse = new AddTradeResponse();
		Trade trade = createTradeModelFromRequest(addTradeRequest);
		try {
			if (tradeId != null) {
				updateTrade(tradeId, trade);
			} else
				executeTrade(trade);
			addTradeResponse.setMessage("Trade Added Successfully");
			tradeRepository.save(trade);
		} catch (final PortfolioTrackingApiException folioTrackingApi) {
			Throwable temp = ExceptionUtils.getCause(folioTrackingApi);
			if (temp instanceof NotEnoughQuantityToSellException)
				throw new NotEnoughQuantityToSellException();
		}
		return ResponseEntity.ok().body(addTradeResponse);

	}

	private void executeTrade(Trade trade) throws PortfolioTrackingApiException {
		Optional<UserPortfolio> userPortfolioDoc = portfolioRepo.findById(USER_ID);
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
			portfolioRepo.save(userPortfolio);
		} else {

			// if SELL Trade
			if (userPortfolio != null) {
				Holding userStockHolding = userPortfolio.getHoldings().get(trade.getStock().getId());
				if (userStockHolding != null) {
					// check if sufficient quantity available to sell
					if (userStockHolding.getQty() >= trade.getQty()) {
						userStockHolding.setQty(userStockHolding.getQty() - trade.getQty());
						userPortfolio.setTotalInvestment(
								userPortfolio.getTotalInvestment() - (trade.getQty() * trade.getPrice()));
						userPortfolio.setCurrentValue(userPortfolio.getCurrentValue()
								- (trade.getQty() * extService.getCurrentStockPrice(trade.getStock())));
						userPortfolio.getHoldings().put(trade.getStock().getId(), userStockHolding);
						portfolioRepo.save(userPortfolio);
					} else {
						// Throw Not enough quantity to sell
						throw new NotEnoughQuantityToSellException();
					}
				} else {
					// Throw Exceptioo Stock Holding Doesn't Exist
					throw new NotEnoughQuantityToSellException();

				}
				userPortfolio.getHoldings().put(trade.getStock().getId(), userStockHolding);
			} else {
				// THROW Exception Portfolio Doesn't Exist
				throw new NotEnoughQuantityToSellException();

			}

		}

	}

	private void updateTrade(String tradeId, Trade userUpdatedTradeModel) throws PortfolioTrackingApiException {
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
						&& (userStockHolding.getQty() - oldTradeModel.get().getQty()) == 0) {
					throw new NotEnoughQuantityToSellException();
				}
			}
		}
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

	private void removeTradeFromUserStockHolding(Trade trade) throws PortfolioTrackingApiException {
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
				}

			} else {
				throw new TradeRemovalException();
			}
		}else {
			userStockHolding.setQty(userStockHolding.getQty()+trade.getQty());
			userPortfolioDoc.get().getHoldings().put(trade.getStock().getId(), userStockHolding);
		}
		//save updated doc
		portfolioRepo.save(userPortfolioDoc.get());
	}
}
