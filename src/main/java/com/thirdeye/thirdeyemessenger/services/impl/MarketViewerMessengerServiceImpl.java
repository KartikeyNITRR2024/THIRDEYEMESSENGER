package com.thirdeye.thirdeyemessenger.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thirdeye.thirdeyemessenger.pojos.LiveStockPayload;
import com.thirdeye.thirdeyemessenger.pojos.ProfitDetails;
import com.thirdeye.thirdeyemessenger.services.MarketViewerMessengerService;
import com.thirdeye.thirdeyemessenger.services.OldMessageService;
import com.thirdeye.thirdeyemessenger.utils.PropertyLoader;
import com.thirdeye.thirdeyemessenger.utils.TimeManagementUtil;

@Service
public class MarketViewerMessengerServiceImpl implements MarketViewerMessengerService {

    @Autowired
    StocksListServiceImpl stocksListServiceImpl;

    @Autowired
    UserInfoServiceImpl userInfoServiceImpl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    TimeManagementUtil timeManagementUtil;
    
    @Value("${telegram_service_url}")
    private String telegramServiceUrl;
    
    @Value("${timeGapInMillis}")
    private String timeGapInMillis;
    
    @Value("${messageLength}")
    private Integer messageLength;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    
    @Autowired
    PropertyLoader propertyLoader;
    
    @Autowired
    OldMessageService oldMessageService;
    
    Map<Long,List<String>> messageHash = new HashMap<>();
    
   
    
    private static final Logger logger = LoggerFactory.getLogger(MarketViewerMessengerServiceImpl.class);
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void messageCreater(LiveStockPayload liveStockPayload) {
    	logger.info("Data is " + liveStockPayload);
        lock.writeLock().lock();
    	try {
	        if (liveStockPayload.getProfitDetailsList() != null && liveStockPayload.getProfitDetailsList().size() > 0) {
	            String stockSymbol = stocksListServiceImpl.getIdToStock(liveStockPayload.getStockId()).replace("&", "_");
	            for (ProfitDetails profitDetails : liveStockPayload.getProfitDetailsList()) {
	//                String message1 = "Price of RELIANCE is changed by 10% in last 30 second. Time : 30:04:34";
	//                String message2 = "Price of RELIANCE is changed by 10 rupees in last 30 second. Time : 30:04:34";
	                String message = String.format("Price of %s is changed by %s%s in last %s seconds. Time : %s.\nOld price %s and New price %s", 
	                                               stockSymbol, 
	                                               profitDetails.getProfit(), 
	                                               profitDetails.getProfitType() == 0 ? "%" : " Rupees", 
	                                               profitDetails.getTimeGap(), 
	                                               timeManagementUtil.getCurrentTimeString(liveStockPayload.getTime()),profitDetails.getOldPrice(),profitDetails.getNewPrice());
	                logger.info("Message is : {}",message);
	                if(messageHash.containsKey(profitDetails.getUserId()))
	                {
	                	List<String> messageList = null;
	                	if(messageHash.get(profitDetails.getUserId()) == null)
	                	{
	                		messageList = new ArrayList<>();
	                	}
	                	else
	                	{
	                		messageList = messageHash.get(profitDetails.getUserId());
	                	}
	                	messageList.add(message);
	                	messageHash.put(profitDetails.getUserId(), messageList);
	                }
	                else
	                {
	                	List<String> messageList = new ArrayList<>();
	                	messageList.add(message);
	                	messageHash.put(profitDetails.getUserId(), messageList);
	                }
	            }
	        }
    	} catch (Exception e) {
            logger.error("Failed to update Message HashMap: {}", e.getMessage(), e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    @Scheduled(fixedRateString = "${timeGapInMillis}")
    public void messageSizeAdjuster() {
        lock.readLock().lock();
        try {
            for (Map.Entry<Long, List<String>> entry : messageHash.entrySet()) {
                List<String> messages = entry.getValue(); // Get the list of messages
//                logger.info("Message size is {}", messages.size());
                if (messages.size() > 0) {
                    StringBuilder message = new StringBuilder();
                    message.append("***Start***  ").append(timeManagementUtil.getCurrentTime()).append("\n\n");
                    
                    Iterator<String> iterator = messages.iterator();
                    
                    while (iterator.hasNext()) {
                        String s = iterator.next();
                        if (message.length() + s.length() > messageLength) {
                            websocketMessageSender(message.toString(), entry.getKey());
                            telegramMessageSender(message.toString(), entry.getKey());
                            oldMessageService.addMessageEntity(message.toString(), entry.getKey().intValue(), 1);
                            message.setLength(0);
                        }
                        message.append(s).append("\n\n");
                        iterator.remove();
                    }
                    
                    message.append("****End****");
                    if (message.length() > 0) {
                        websocketMessageSender(message.toString(), entry.getKey());
                        telegramMessageSender(message.toString(), entry.getKey());
                        oldMessageService.addMessageEntity(message.toString(), entry.getKey().intValue(), 1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error in size limiting of message", e);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public void telegramMessageSender(String message, Long userId) {
        try {
//            logger.info("Message is : {}", message);
        	if(userInfoServiceImpl.getIdToUser(userId).getIsActive().equals(1)) {
	            String telegramApiUrl = String.format("%s/bot%s/sendMessage?chat_id=@%s&text=%s",
	                    telegramServiceUrl, propertyLoader.telegramBotToken, 
	                    userInfoServiceImpl.getIdToUser(userId).getTelegramGroupId1(), 
	                    message);
	            restTemplate.getForObject(telegramApiUrl, String.class);
	            logger.info("Message has been sent to userId: {} using telegram", userId);
            }
        	else
        	{
        		logger.info("User {} is not active", userId);
        	}
        } catch (Exception e) {
            logger.error("Could not send Message", e);
            logger.error("Message has been sent to userId: {} using telegram", userId);
        }
    }
    
    @Override
    public void websocketMessageSender(String message, Long userId) {
        try {
//            logger.info("Message is : {}", message);
        	if(userInfoServiceImpl.getIdToUser(userId).getIsActive().equals(1)) {
	            String destination = "/marketviewer/" + userId;
	            simpMessagingTemplate.convertAndSend(destination, message);
	            logger.info("Message has been sent to userId: {} using websocket", userId);
            }
        	else
        	{
        		logger.info("User {} is not active", userId);
        	}
        } catch (Exception e) {
            logger.error("Could not send Message", e);
            logger.error("Message has been not sent to userId: {} using websocket", userId);
        }
    }


//    @Override
//    public void telegramMessageSender(String message, Long userId) {
//    	CompletableFuture.runAsync(() -> {
//        try {
////            logger.info("Message is : {}", message);
//            String telegramApiUrl = String.format("%s/bot%s/sendMessage?chat_id=@%s&text=%s",
//                    telegramServiceUrl, propertyLoader.telegramBotToken, 
//                    userInfoServiceImpl.getIdToUser(userId).getTelegramGroupId1(), 
//                    message);
//            restTemplate.getForObject(telegramApiUrl, String.class);
//            logger.info("Message has been sent");
//        } catch (Exception e) {
//            logger.error("Could not send Message", e);
//        }
//    	});
//    }

}
