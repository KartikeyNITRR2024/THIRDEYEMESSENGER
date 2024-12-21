package com.thirdeye.thirdeyemessenger.services.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thirdeye.thirdeyemessenger.entity.UserInfo;
import com.thirdeye.thirdeyemessenger.pojos.Changes;
import com.thirdeye.thirdeyemessenger.services.MorningPriceUpdaterMessengerService;
import com.thirdeye.thirdeyemessenger.utils.PropertyLoader;
import com.thirdeye.thirdeyemessenger.utils.TimeManagementUtil;

@Service
public class MorningPriceUpdaterMessengerServiceImpl implements MorningPriceUpdaterMessengerService {
	
    @Autowired
    private StocksListServiceImpl stocksListServiceImpl;

    @Autowired
    private UserInfoServiceImpl userInfoServiceImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TimeManagementUtil timeManagementUtil;

    @Value("${telegram_service_url}")
    private String telegramServiceUrl;
    
    @Value("${messageLength}")
    private Integer messageLength;

    @Autowired
    private PropertyLoader propertyLoader;

    private static final Logger logger = LoggerFactory.getLogger(HoldedStockMessengerServiceImpl.class);

	@Override
	public void telegramMessageCreater(List<Changes> changeInPriceList) throws Exception {
		
		try {
			String message = String.format("Time : %s\n\n", 
	                timeManagementUtil.getCurrentTimeString());
			
			for(Changes changes : changeInPriceList)
			{
				String message1 = "";
				message1 += String.format("Stock name : %s\n", stocksListServiceImpl.getIdToStock(changes.getStockId()).replace("&", "_"));
				message1 += String.format("Old Price : %.2f\n", changes.getOldPrice());
				message1 += String.format("New Price : %.2f\n", changes.getNewPrice());
				message1 += String.format("Price Change : %.3f\n", changes.getChanges());
				message1 += String.format("Percent Price Change: %.3f%%\n\n", changes.getPercentChanges());
				
				if(message.length() + message1.length() <= messageLength)
				{
					message += message1;
				}
				else
				{
					telegramMessageSender(message);
					message = "";
				}
			}
			if(message.length() > 0)
			{
				telegramMessageSender(message);
			}
		} catch (Exception ex)
		{
			logger.error("Error in creating message: {}", ex);
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public void telegramMessageSender(String message) throws Exception {
	    Map<Long, UserInfo> users = userInfoServiceImpl.getAllUsers();
	    
	    AtomicInteger failedCount = new AtomicInteger(0);

	    users.forEach((userId, userInfo) -> {
	        try {
	        	if(userInfo.getIsActive().equals(1))
            	{
		            String telegramApiUrl = String.format("%s/bot%s/sendMessage?chat_id=@%s&text=%s",
		                telegramServiceUrl, propertyLoader.telegramBotToken,
		                userInfo.getTelegramGroupId3(),
		                message);
		            restTemplate.getForObject(telegramApiUrl, String.class);
		            logger.info("Message has been sent to userId: {}", userId);
            	}else
            	{
            		logger.info("User {} is not active", userId);
            	}
	        } catch (Exception e) {
	            failedCount.incrementAndGet();
	            logger.error("Could not send message to userId: {}", userId, e);
	        }
	    });
	    
	    if (failedCount.get() > 0) {
	        throw new Exception("Failed to send message to " + failedCount.get() + " users.");
	    }
	}



}
