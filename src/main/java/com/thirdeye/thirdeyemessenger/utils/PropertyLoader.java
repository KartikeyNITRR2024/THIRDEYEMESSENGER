package com.thirdeye.thirdeyemessenger.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thirdeye.thirdeyemessenger.repositories.ConfigUsedRepo;
import com.thirdeye.thirdeyemessenger.entity.ConfigUsed;
import com.thirdeye.thirdeyemessenger.entity.ConfigTable;
import com.thirdeye.thirdeyemessenger.repositories.ConfigTableRepo;

@Component 
public class PropertyLoader {
    public String telegramBotToken;
    public long timeGap;
    private Long configId;
    
    @Autowired
    private ConfigUsedRepo configUsedRepo;

    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    @Autowired
    private ConfigTableRepo configTableRepo;

    public void updatePropertyLoader() {
        try {
        	logger.info("Fetching currently config used.");
            ConfigUsed configUsed = configUsedRepo.findById(1L).get();
            configId = configUsed.getId();
            logger.debug("Fetching configuration for configId: {}", configId);
            Optional<ConfigTable> configTable = configTableRepo.findById(configId);
            if (configTable.isPresent()) {
            	telegramBotToken = configTable.get().getTelegramBotToken();
            	timeGap = configTable.get().getTimeGap();
                logger.info("Telegram Bot Token loaded: {}", telegramBotToken);
            } else {
                logger.warn("No configuration found for configId: {}", configId);
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching configuration: {}", e.getMessage(), e);
        }
    }
}
