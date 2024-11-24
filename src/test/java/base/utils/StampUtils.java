package base.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class StampUtils {
	
	public static String getCurrentDateTimestamp() {
		LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT+7"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		return now.format(formatter);
	}
	
	public static String getCurrentDatestamp() {
		LocalDate now = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return now.format(formatter);
	}
	
	public static String getCurrentTimeStamp() {
		LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
		return now.format(formatter);
	}

}
