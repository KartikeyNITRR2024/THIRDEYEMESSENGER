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

import com.thirdeye.thirdeyemessenger.pojos.LiveStockPayload;
import com.thirdeye.thirdeyemessenger.services.impl.MarketViewerMessengerServiceImpl;
import com.thirdeye.thirdeyemessenger.utils.AllMicroservicesData;


@RestController
@RequestMapping("/api/marketviewermessenger")
public class MarketViewerMessengerController {

	@Autowired
	AllMicroservicesData allMicroservicesData;
	
	@Autowired
	MarketViewerMessengerServiceImpl marketViewerMessengerServiceImpl;
	
	
    private static final Logger logger = LoggerFactory.getLogger(StatusCheckerController.class);

    @PostMapping("/{uniqueId}")
    public ResponseEntity<Boolean> sendMarketViewerData(@PathVariable("uniqueId") Integer pathUniqueId, @RequestBody LiveStockPayload liveStockPayload) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {}: Found", allMicroservicesData.current.getMicroserviceUniqueId());
            marketViewerMessengerServiceImpl.messageCreater(liveStockPayload);
            return ResponseEntity.ok(true);
        } else {
            logger.warn("Status check for uniqueId {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
}

