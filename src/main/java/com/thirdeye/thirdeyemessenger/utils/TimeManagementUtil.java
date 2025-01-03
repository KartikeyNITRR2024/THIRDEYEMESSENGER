package com.thirdeye.thirdeyemessenger.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class TimeManagementUtil {
	
	public Timestamp getCurrentTime() {
        ZonedDateTime indianTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDateTime localDateTime = indianTime.toLocalDateTime();
        return Timestamp.valueOf(localDateTime);
    }
	
	public String getCurrentTimeString() {
        ZonedDateTime zonedTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return zonedTime.format(formatter);
    }
	
	public String getCurrentTimeString(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        ZonedDateTime zonedTime = instant.atZone(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return zonedTime.format(formatter);
    }

}
