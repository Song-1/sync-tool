/**
 * 
 */
package cn.com.musicone.www.counts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 *
 */
public class CountsGetIdUtil {
	
	public static String REGEX_SINGER_PATH = "/api/v3/enjoy/singers/(\\d+)/songs";
	public static String REGEX_ALBUM_PATH = "/api/v3/enjoy/albums/(\\d+)/songs";
	public static String REGEX_ENJOYCD_PATH = "/api/v3/play/enjoycd/(\\d+)";
	public static String REGEX_CHERRYTIME_PATH = "/api/v3/play/cherrytime/(\\d+)";
	public static String START_DATE_STR = " 00:00:00";
	public static String END_DATE_STR = " 23:59:59";
	public static final long MEMBER_LOGIN_EXPIRATION_TIMES_DAY = 86400000L;// 一天
	
	public static int getId(String str,String regEx){
		if(StringUtils.isBlank(str)){
			return 0;
		}else if(StringUtils.isBlank(regEx)){
			return 0;
		}
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if(m.find()){
			String idStr = m.group(1).trim();
			return isTypeOfInt(idStr);
		}
		return 0;
	}
	
	public static int isTypeOfInt(String str){
		if(StringUtils.isBlank(str)){
			return -1;
		}
		str = str.trim();
		String regEx =  "^\\d+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if(m.find()){
			return Integer.parseInt(str);
		}
		return -1;
	}
	
	public static String getStartDate(Date date){
		String dateStr = getFormatDate(date);
		if(StringUtils.isBlank(dateStr)){
			return dateStr;
		}
		return dateStr + START_DATE_STR;
	}
	public static String getEndDate(Date date){
		String dateStr = getFormatDate(date);
		if(StringUtils.isBlank(dateStr)){
			return dateStr;
		}
		return dateStr + END_DATE_STR;
	}
	
	public static String getFormatDate(Date date){
		if(date == null){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(date);
		return dateStr;
	}
	
	public static Date getDateFromStr(String str){
		if(StringUtils.isBlank(str)){
			return null;
		}
		return getDateFromStr(str, null);
	}
	public static Date getDateFromStr(String str,String pattren){
		if(StringUtils.isBlank(str)){
			return null;
		}
		if(StringUtils.isBlank(pattren)){
			pattren = "yyyy-MM-dd";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattren);
		try {
			Date date = format.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
