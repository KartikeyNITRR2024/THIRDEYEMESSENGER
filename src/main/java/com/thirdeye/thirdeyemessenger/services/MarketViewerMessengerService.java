package com.thirdeye.thirdeyemessenger.services;

import com.thirdeye.thirdeyemessenger.pojos.LiveStockPayload;

public interface MarketViewerMessengerService {
	void telegramMessageSender(String message, Long userId);
	void messageSizeAdjuster();
	void messageCreater(LiveStockPayload liveStockPayload);
	void websocketMessageSender(String message, Long userId);
}
