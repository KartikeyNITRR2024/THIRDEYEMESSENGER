package com.thirdeye.thirdeyemessenger.services;

public interface StocksListService {
	public void getStockListInBatches() throws Exception;
	public Integer getStocksSize();
	public String getIdToStock(Long stockId);
}
