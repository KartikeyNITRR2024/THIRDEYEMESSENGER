package com.thirdeye.thirdeyemessenger.pojos;

import java.sql.Timestamp;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LiveStockPayload {
	private Integer batchId;
	private Long stockId;
	private Timestamp time;
	private Double price;
	private ArrayList<ProfitDetails> profitDetailsList;
}
