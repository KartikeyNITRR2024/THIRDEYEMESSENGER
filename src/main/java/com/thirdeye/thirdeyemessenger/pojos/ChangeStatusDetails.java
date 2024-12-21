package com.thirdeye.thirdeyemessenger.pojos;

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
public class ChangeStatusDetails {
   private Long statusId;
   private Double statusPrice;
   private Integer status;
}
