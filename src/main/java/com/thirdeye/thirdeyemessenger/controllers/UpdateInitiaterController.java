package com.thirdeye.thirdeyemessenger.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thirdeye.thirdeyemessenger.services.UpdateInitiaterService;
import com.thirdeye.thirdeyemessenger.utils.AllMicroservicesData;


@RestController
@RequestMapping("/api/update")
public class UpdateInitiaterController {

	@Autowired
	AllMicroservicesData allMicroservicesData;
	
	@Autowired
	private UpdateInitiaterService updateInitiaterService;
	
	@Value("${updaterMicroserviceName}")
    private String updaterMicroserviceName;
	
    private static final Logger logger = LoggerFactory.getLogger(UpdateInitiaterController.class);

    @PostMapping("/{uniqueId}/{uniqueIdOfSender}")
    public ResponseEntity<Boolean> updateAll(@PathVariable("uniqueId") Integer pathUniqueId, @PathVariable("uniqueIdOfSender") Integer pathUniqueIdOfSender) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId()) && pathUniqueIdOfSender.equals(allMicroservicesData.allMicroservices.get(updaterMicroserviceName).getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {} and uniqueIdOfSender {}: Found", allMicroservicesData.current.getMicroserviceUniqueId(), allMicroservicesData.allMicroservices.get(updaterMicroserviceName).getMicroserviceUniqueId());
			try {
				updateInitiaterService.initiateUpdate();
			} catch (Exception e) {
				return ResponseEntity.ok(Boolean.FALSE);
			}
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            logger.warn("Status check for uniqueId {} or uniqueIdOfSender {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId(), allMicroservicesData.allMicroservices.get(updaterMicroserviceName).getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }    
}

