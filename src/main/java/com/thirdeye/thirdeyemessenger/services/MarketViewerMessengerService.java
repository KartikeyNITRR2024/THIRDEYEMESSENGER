package com.thirdeye.thirdeyemessenger.services;

import com.thirdeye.thirdeyemessenger.pojos.LiveStockPayload;

public interface MarketViewerMessengerService {
	void telegramMessageCreater(LiveStockPayload liveStockPayload);
	void telegramMessageSender(String message, Long userId);
	void telegramMessageSizeAdjuster();
}
