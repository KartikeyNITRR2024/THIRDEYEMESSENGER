package com.thirdeye.thirdeyemessenger.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thirdeye.thirdeyemessenger.entity.Stocks;
import com.thirdeye.thirdeyemessenger.repositories.StocksRepo;
import com.thirdeye.thirdeyemessenger.services.StocksListService;

@Service
public class StocksListServiceImpl implements StocksListService {

    private Map<Long, String> idToStock = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(StocksListServiceImpl.class);
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    private StocksRepo stocksRepo;
    
    @Value("${batchSizeToGetSetFromDatabase}")
    private Long batchSizeToGetSetFromDatabase;

    public void getStockListInBatches() throws Exception {
    	Map<Long, String> idToStock1 = new HashMap<>();
        logger.info("Starting to fetch stock list in batches.");
        try {
            long totalStocks = stocksRepo.count();
            int pageSize = batchSizeToGetSetFromDatabase.intValue();
            int totalPages = (int) Math.ceil((double) totalStocks / pageSize);
            for (int page = 0; page < totalPages; page++) {
                Pageable pageable = PageRequest.of(page, pageSize);
                Page<Stocks> stockListBatch = stocksRepo.findAll(pageable);
                for (Stocks stock : stockListBatch.getContent()) { 
                	idToStock1.put(stock.getId(), stock.getStockSymbol() + " " + stock.getMarketName());
                }
            }
            lock.writeLock().lock();
            idToStock.clear();
            idToStock = new HashMap<>(idToStock1);
            logger.info("Successfully fetched all {} stocks in batches.", totalStocks);
        } catch (Exception e) {
            logger.error("Error occurred while fetching stock list in batches: {}", e.getMessage(), e);
            throw new Exception("Failed to retrieve stock list in batches", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public String getIdToStock(Long stockId) {
    	String stockSymbol = "";
    	lock.readLock().lock();
        try {
	    	if(idToStock.containsKey(stockId))
	    	{
	    		stockSymbol = idToStock.get(stockId);
	    	}
	        return stockSymbol;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public Integer getStocksSize() {
        lock.readLock().lock();
        try {
            return idToStock.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
