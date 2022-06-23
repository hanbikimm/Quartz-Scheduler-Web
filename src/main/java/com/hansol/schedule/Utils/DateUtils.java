package com.hansol.schedule.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
	private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static Date convert(LocalDateTime date) {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Date convert(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		
		return formatter.parse(date);
	}
	
	public static LocalDateTime convert(Date date) {
		return date.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	public static String toString(Date date) {
        return toString(DEFAULT_DATETIME_FORMAT, date);
    }

    public static String toString(String pattern, Date date) {
        String dateStr = null;
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            dateStr = formatter.format(date);
        }
        
        return dateStr;
    }
}