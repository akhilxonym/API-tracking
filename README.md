# Portfolio-tracking-API
Simple Portfolio tracking project

**API ROUTES** <br/>
Root URL: https://assignment-portfolio-tracking.herokuapp.com <br/>
1. Login <br/>
path: /login | POST Request Body (Form-Data) <br/>
username:Adarsh <br/>
password:Adarsh@smallcase
-------------------
![](api-tracking/test-images/login_ss_smallcase.png)

2. Stock <br/>
path: /stock/all | GET Request <br/><br/>
Description: This endpoint will fetch the list of stock models from the database. Right now I've saved 3 stocks as directed in the assignment document.<br/>You may leaverage this end point to make the Request body for add or update stock trade.<br/> I could've used only the stock ids in the request body but I've this flow in mind that a user while placing a add trade order UI must be having a stock model in place.<br/>
-------------------
![](api-tracking/test-images/allStocks_ss_smallcase.png) <br/>
path: /stock?stockId= <br/>
Gets the stock with that specific id <br/> <br/>

3. Trade Details <br>
path: /trade/all , GET request <br/>
![](api-tracking/test-images/allTrades_ss_smallcase.png) <br/>
path: /trade?tradeId= (to get specific trade) <br/><br/>
Description: This endpoint can be utilized to show the list of trades to the user. <br/><br/>

4. Add a trade [assumed an obvious flow in UI that going to that particular stock first then placing the order] <br/>
path: /stock/trade/order | POST request <br/><br/>
Description: All necessary validations I've placed. Only validated input request will go inside the service logic. Proper exception with the corresponding message with HTTP status bad request will be returned in case of Negative scenarios. Please leaverage /stock/all endpoint to get the desired stock model and along with that you need to add qty,tradeType and price fields in request body. <br/>
![](api-tracking/test-images/stock_addtrade_ss_smallcase.png)


5. Edit a trade <br/>
path: /stock/trade/order?tradeId= | POST Request<br/><br/>
Description: Probably the most puzzling part of application. Here I've covered all the scenarios and required validations. Here, complete removal of OldTrade will happen when: 1.Stocks (ids) are different 2. If stocks are same then tradeType is different. Oldtrade needed to be removed while simultaneously checking if it can be removed or not and then only new trade order can be placed. (if it could) ELSE difference in quantities are calculated and BUY or Sell cases are handled sepereately. 
![](api-tracking/test-images/tradeUpdate_ss_smallcase.png) <br/>


6. Remove a Trade <br/>
path: /stock/trade/remove , DELETE request <br/>
![](api-tracking/test-images/removeTrade_ss_smallcase.png) <br/>
7. UserPortfolio <br/>
path: /portfolio | GET request <br/>
![](api-tracking/test-images/userPortfolio_ss_smallcase.png) <br/>




