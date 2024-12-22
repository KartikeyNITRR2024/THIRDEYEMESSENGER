package com.thirdeye.thirdeyemessenger.services;

import java.util.List;

import com.thirdeye.thirdeyemessenger.pojos.OldMessage;

public interface OldMessageService {

	void addMessageEntity(String messageText, Integer userid, Integer typeofMessage) throws Exception;

	List<OldMessage> getOldMessage(Integer userid, Integer typeofMessage) throws Exception;

	void deleteAllMessage();

}
