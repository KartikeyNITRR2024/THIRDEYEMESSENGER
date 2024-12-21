package com.thirdeye.thirdeyemessenger.services;

import com.thirdeye.thirdeyemessenger.pojos.HoldedStockPayload;

public interface HoldedStockMessengerService {

	void telegramMessageCreater(HoldedStockPayload holdedStockPayload);

}
