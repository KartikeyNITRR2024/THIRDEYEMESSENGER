package com.thirdeye.thirdeyemessenger.services;

import java.util.List;

import com.thirdeye.thirdeyemessenger.pojos.Changes;

public interface MorningPriceUpdaterMessengerService {
	void telegramMessageCreater(List<Changes> changeInPriceList) throws Exception;
	void telegramMessageSender(String message) throws Exception;
}
