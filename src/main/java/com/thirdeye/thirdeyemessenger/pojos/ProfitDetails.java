package com.thirdeye.thirdeyemessenger.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfitDetails {
   private Long userId;
   private Double profit;
   private Integer profitType;
   private Long timeGap;
   private Double oldPrice;
   private Double newPrice;
}
