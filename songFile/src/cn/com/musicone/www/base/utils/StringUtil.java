/**
 * 
 */
package cn.com.musicone.www.base.utils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Administrator
 *
 */
public class StringUtil extends StringUtils {
	protected static final Logger logger = LogManager
			.getLogger(StringUtil.class);

	public static long formateLongStr(String str) {
		if (isBlank(str)) {
			return 0L;
		}
		try {
			long l = Long.parseLong(str);
			return l;
		} catch (Exception e) {
			logger.error("将字符串转换成long类型错误,字符串::" + str);
			logger.error(e.getMessage(), e);
		}
		return 0L;
	}

	public static int formateIntStr(String str) {
		if (isBlank(str)) {
			return 0;
		}
		try {
			int i = Integer.parseInt(str);
			return i;
		} catch (Exception e) {
			logger.error("将字符串转换成int类型错误,字符串::" + str);
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	public static String formateDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (isBlank(pattern)) {
			pattern = "yyyy-MM-dd hh:mm:ss";
		}
		SimpleDateFormat formate = new SimpleDateFormat(pattern);
		return formate.format(date);
	}

	public static String encodeString(String str, String enc) throws Exception {
		if (str == null) {
			return "";
		}
		String theDefenc = "UTF-8";
		if (!isBlank(enc)) {
			theDefenc = enc;
		}
		return URLEncoder.encode(str, theDefenc);
	}

	public static String encodeStr(String str, String enc) {
		try {
			return encodeString(str, enc);
		} catch (Exception e) {
			return str;
		}
	}
}
