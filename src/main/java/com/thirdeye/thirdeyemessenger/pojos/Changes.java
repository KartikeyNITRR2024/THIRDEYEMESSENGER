package com.thirdeye.thirdeyemessenger.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Changes {
	private Long stockId;
    private Double oldPrice;
    private Double newPrice;
    private Double changes;
    private Double percentChanges;
}
