package com.thirdeye.thirdeyemessenger.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye.thirdeyemessenger.pojos.HoldedStockPayload;
import com.thirdeye.thirdeyemessenger.pojos.LiveStockPayload;
import com.thirdeye.thirdeyemessenger.services.impl.HoldedStockMessengerServiceImpl;
import com.thirdeye.thirdeyemessenger.services.impl.MarketViewerMessengerServiceImpl;
import com.thirdeye.thirdeyemessenger.utils.AllMicroservicesData;


@RestController
@RequestMapping("/api/holdedstockmessenger")
public class HoldedStockMessengerController {

	@Autowired
	AllMicroservicesData allMicroservicesData;
	
	@Autowired
	HoldedStockMessengerServiceImpl holdedStockMessengerServiceImpl;
	
	
    private static final Logger logger = LoggerFactory.getLogger(StatusCheckerController.class);

    @PostMapping("/{uniqueId}")
    public ResponseEntity<Boolean> sendHoldedStockData(@PathVariable("uniqueId") Integer pathUniqueId, @RequestBody HoldedStockPayload holdedStockPayload) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {}: Found", allMicroservicesData.current.getMicroserviceUniqueId());
            holdedStockMessengerServiceImpl.telegramMessageCreater(holdedStockPayload);
            return ResponseEntity.ok(true);
        } else {
            logger.warn("Status check for uniqueId {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
}


