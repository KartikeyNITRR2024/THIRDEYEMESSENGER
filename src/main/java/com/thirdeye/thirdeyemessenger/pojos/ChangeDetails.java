package com.thirdeye.thirdeyemessenger.pojos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDetails {
	private Long userId;
	private Integer changeType;
	List<ChangeStatusDetails> statusList = new ArrayList<>();
	private Double newPrice;
	private Double buyingPrice;
}
