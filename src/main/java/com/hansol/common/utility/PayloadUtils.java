package com.hansol.common.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadUtils {
	public static final String NO_VALUE = "N/A";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static Boolean hasToken(String token) {
		if (StringUtils.isEmpty(token)) return false;
		if (token.equals("undefined")) return false;
		
		return true;
	}
	
	public static Object isEmpty(Object object) {
		return ObjectUtils.isEmpty(object) ? NO_VALUE : object;
	}
	
	public static String isEmpty(String string) {
		return StringUtils.isEmpty(string) ? NO_VALUE : string;
	}
	
	public static String isNull(Number number) {
		return (number == null) ? NO_VALUE : String.valueOf(number);
	}
	
	public static String toString(Date date) {
		return (date == null) ? NO_VALUE : toString(DATETIME_FORMAT, date);
    }

    public static String toString(String pattern, Date date) {
    	if (date == null) return NO_VALUE;
    	
    	SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    	return formatter.format(date);
    }
}