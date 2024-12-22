package com.thirdeye.thirdeyemessenger.pojos;

import java.sql.Timestamp;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OldMessage {
	private String messageText;
	private Timestamp timeofgenerating;
}
