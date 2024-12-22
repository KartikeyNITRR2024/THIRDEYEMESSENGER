package com.thirdeye.thirdeyemessenger.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye.thirdeyemessenger.annotation.AdminRequired;
import com.thirdeye.thirdeyemessenger.pojos.OldMessage;
import com.thirdeye.thirdeyemessenger.services.OldMessageService;
import com.thirdeye.thirdeyemessenger.utils.AllMicroservicesData;

@RestController
@RequestMapping("/api/oldmessage")
public class OldMessageController {

    @Autowired
    private AllMicroservicesData allMicroservicesData;

    @Autowired
    private OldMessageService oldMessageService;

    private static final Logger logger = LoggerFactory.getLogger(OldMessageController.class);

    @GetMapping("get/{typeofmessage}/{userid}/{uniqueId}")
    public ResponseEntity<List<OldMessage>> getOldMessages(
            @PathVariable("uniqueId") Integer pathUniqueId,
            @PathVariable("typeofmessage") Integer typeofmessage,
            @PathVariable("userid") Integer userid) {

        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {}: Found", allMicroservicesData.current.getMicroserviceUniqueId());

            try {
                List<OldMessage> messages = oldMessageService.getOldMessage(userid, typeofmessage);
                return ResponseEntity.ok(messages);
            } catch (Exception ex) {
                logger.error("Error fetching old messages: {}", ex.getMessage());
                return ResponseEntity.internalServerError().build();
            }
        } else {
            logger.warn("Status check for uniqueId {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("delete/{uniqueId}")
    @AdminRequired
    public ResponseEntity<Void> deleteAllOldMessage(@PathVariable("uniqueId") Integer pathUniqueId) {

        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {}: Found", allMicroservicesData.current.getMicroserviceUniqueId());

            try {
                oldMessageService.deleteAllMessage();
                return ResponseEntity.ok().build();
            } catch (Exception ex) {
                logger.error("Error deleting messages: {}", ex.getMessage());
                return ResponseEntity.internalServerError().build();
            }
        } else {
            logger.warn("Status check for uniqueId {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }

    
    
}
