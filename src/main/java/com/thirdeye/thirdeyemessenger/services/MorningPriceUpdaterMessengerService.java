package com.thirdeye.thirdeyemessenger.services;

import java.util.List;
import java.util.Map;

import com.thirdeye.thirdeyemessenger.entity.UserInfo;
import com.thirdeye.thirdeyemessenger.pojos.Changes;

public interface MorningPriceUpdaterMessengerService {
	void messageCreater(List<Changes> changeInPriceList) throws Exception;
	void telegramMessageSender(String message, Map<Long, UserInfo> users) throws Exception;
	void websocketMessageSender(String message, Map<Long, UserInfo> users) throws Exception;
}
