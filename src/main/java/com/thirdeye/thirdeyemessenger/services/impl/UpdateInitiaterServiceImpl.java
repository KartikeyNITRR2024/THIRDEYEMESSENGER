package com.thirdeye.thirdeyemessenger.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye.thirdeyemessenger.services.UpdateInitiaterService;
import com.thirdeye.thirdeyemessenger.utils.Initiatier;

@Service
public class UpdateInitiaterServiceImpl implements UpdateInitiaterService {
	
	@Autowired
	Initiatier initiatier;
	
	private static final Logger logger = LoggerFactory.getLogger(UpdateInitiaterServiceImpl.class);
	
	@Override
	public void initiateUpdate() throws Exception {
        try {
        	logger.info("Going to update initiate");
            initiatier.init();
            logger.info("Successfully updated initiate");
        } catch (Exception e) {
            e.printStackTrace(); 
            logger.info("Failed to updated initiate");
            throw new Exception("Failed to updated initiate");
        }
    }
}
