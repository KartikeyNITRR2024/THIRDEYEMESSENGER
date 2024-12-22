package com.thirdeye.thirdeyemessenger.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thirdeye.thirdeyemessenger.services.OldMessageService;
import com.thirdeye.thirdeyemessenger.utils.TimeManagementUtil;
import com.thirdeye.thirdeyemessenger.entity.MessageEntity;
import com.thirdeye.thirdeyemessenger.pojos.OldMessage;
import com.thirdeye.thirdeyemessenger.repositories.MessageEntityRepo;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class OldMessageServiceImpl implements OldMessageService {

    @Autowired
    private MessageEntityRepo messageEntityRepo;

    @Autowired
    private TimeManagementUtil timeManagementUtil;

    private static final Logger logger = LoggerFactory.getLogger(OldMessageServiceImpl.class);

    @Override
    public void addMessageEntity(String messageText, Integer userid, Integer typeofMessage) throws Exception {
        try {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMessageText(messageText);
            messageEntity.setUserId(userid);
            messageEntity.setTypeOfMessage(typeofMessage);
            messageEntity.setTimeofgenerating(timeManagementUtil.getCurrentTime());
            messageEntityRepo.save(messageEntity);
        } catch (Exception ex) {
            logger.info("Failed to add message in database: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public List<OldMessage> getOldMessage(Integer userid, Integer typeofMessage) throws Exception {
        List<OldMessage> oldMessages = new ArrayList<>();
        try {
            List<MessageEntity> messages = messageEntityRepo.findByUserIdAndTypeOfMessageOrderByTimeofgenerating(userid, typeofMessage);
            for (MessageEntity messageEntity : messages) {
                oldMessages.add(new OldMessage(messageEntity.getMessageText(), messageEntity.getTimeofgenerating()));
            }
        } catch (Exception ex) {
            logger.info("Failed to fetch messages from the database: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        return oldMessages;
    }

    @Override
    public void deleteAllMessage() {
        messageEntityRepo.deleteAll();
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kolkata")
    public void deleteMessagesAtMidnight() {
        try {
            logger.info("Message cleanup started at: " + ZonedDateTime.now(ZoneId.of("Asia/Kolkata")));
            deleteAllMessage();
            logger.info("All messages successfully deleted.");
        } catch (Exception ex) {
            logger.error("Error during message cleanup: " + ex.getMessage(), ex);
        }
    }
}
