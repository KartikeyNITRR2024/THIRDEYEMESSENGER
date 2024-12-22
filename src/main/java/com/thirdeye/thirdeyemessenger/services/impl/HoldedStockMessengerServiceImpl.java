package com.thirdeye.thirdeyemessenger.services.impl;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thirdeye.thirdeyemessenger.pojos.ChangeDetails;
import com.thirdeye.thirdeyemessenger.pojos.ChangeStatusDetails;
import com.thirdeye.thirdeyemessenger.pojos.HoldedStockPayload;
import com.thirdeye.thirdeyemessenger.services.HoldedStockMessengerService;
import com.thirdeye.thirdeyemessenger.services.OldMessageService;
import com.thirdeye.thirdeyemessenger.utils.PropertyLoader;
import com.thirdeye.thirdeyemessenger.utils.TimeManagementUtil;

@Service
public class HoldedStockMessengerServiceImpl implements HoldedStockMessengerService {

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

    @Autowired
    private PropertyLoader propertyLoader;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
    private OldMessageService oldMessageService;

    private static final Logger logger = LoggerFactory.getLogger(HoldedStockMessengerServiceImpl.class);

    @Override
    public void messageCreater(HoldedStockPayload holdedStockPayload) {
        List<ChangeDetails> changeDetailsList = holdedStockPayload.getChangeDetailsList();
        if (changeDetailsList != null && !changeDetailsList.isEmpty()) {
            String stockSymbol = stocksListServiceImpl.getIdToStock(holdedStockPayload.getStockId()).replace("&", "_");

            for (ChangeDetails changeDetails : changeDetailsList) {
                String message = String.format("Status of %s is changed.\nTime : %s\n", stockSymbol,
                        timeManagementUtil.getCurrentTimeString(holdedStockPayload.getTime()));
                
                boolean foundNew = false;

                for (ChangeStatusDetails changeStatusDetails : changeDetails.getStatusList()) {
                    String status = "";
                    if (changeStatusDetails.getStatus() != null) {
                        status = (changeStatusDetails.getStatus() == 1) ? "NEW" : "OLD";
                        foundNew = (changeStatusDetails.getStatus() == 1) || foundNew; // Mark if foundNew
                    }

                    String messagePart = String.format("%s   %s%s   %s\n", 
                        changeStatusDetails.getStatusId(),
                        changeStatusDetails.getStatusPrice(),
                        changeDetails.getChangeType() == 0 ? "%" : "Rupees", 
                        status);
                    message += messagePart;
                }

                if (!foundNew) {
                    message += "Drop below lowest\n";
                }
                
                try {
                	if(userInfoServiceImpl.getIdToUser(changeDetails.getUserId()).getIsActive().equals(1))
                	{
    		            String destination = "/marketviewer/" + changeDetails.getUserId();
    		            simpMessagingTemplate.convertAndSend(destination, message);
	                    logger.info("Message has been sent using websocket {} :"+changeDetails.getUserId());
                	}
                	else
                	{
                		logger.info("User {} is not active", changeDetails.getUserId());
                	}
                } catch (Exception e) {
                    logger.error("Could not send Message", e);
                    logger.error("Message has been not sent using websocket {} :"+changeDetails.getUserId());
                }

                try {
                	if(userInfoServiceImpl.getIdToUser(changeDetails.getUserId()).getIsActive().equals(1))
                	{
	                    String telegramApiUrl = String.format("%s/bot%s/sendMessage?chat_id=@%s&text=%s",
	                            telegramServiceUrl, 
	                            propertyLoader.telegramBotToken,
	                            userInfoServiceImpl.getIdToUser(changeDetails.getUserId()).getTelegramGroupId2(),
	                            message);
	                    restTemplate.getForObject(telegramApiUrl, String.class);
	                    logger.info("Message has been sent using telegram {} :"+changeDetails.getUserId());
                	}
                	else
                	{
                		logger.info("User {} is not active", changeDetails.getUserId());
                	}
                } catch (Exception e) {
                    logger.error("Could not send Message", e);
                    logger.error("Message has been not sent using telegram {} :"+changeDetails.getUserId());
                }
                try {
					oldMessageService.addMessageEntity(message, changeDetails.getUserId().intValue(), 3);
				} catch (Exception e) {
					e.printStackTrace();
				}
                
            }
        }
    }
}
