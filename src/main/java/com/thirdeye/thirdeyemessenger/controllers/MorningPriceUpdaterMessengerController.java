package com.thirdeye.thirdeyemessenger.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye.thirdeyemessenger.pojos.Changes;
import com.thirdeye.thirdeyemessenger.services.MorningPriceUpdaterMessengerService;
import com.thirdeye.thirdeyemessenger.utils.AllMicroservicesData;


@RestController
@RequestMapping("/api/morningpriceupdatermessenger")
public class MorningPriceUpdaterMessengerController {

	@Autowired
	AllMicroservicesData allMicroservicesData;
	
	@Autowired
	MorningPriceUpdaterMessengerService morningPriceUpdaterMessengerService;
	
    private static final Logger logger = LoggerFactory.getLogger(MorningPriceUpdaterMessengerController.class);

    @PostMapping("/{uniqueId}")
    public ResponseEntity<Boolean> morningPriceUpdaterData(@PathVariable("uniqueId") Integer pathUniqueId, @RequestBody List<Changes> changeInPriceList) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {}: Found", allMicroservicesData.current.getMicroserviceUniqueId());
            try {
				morningPriceUpdaterMessengerService.messageCreater(changeInPriceList);
			} catch (Exception e) {
				return ResponseEntity.ok(false);
			}
            return ResponseEntity.ok(true);
        } else {
            logger.warn("Status check for uniqueId {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
    
}


