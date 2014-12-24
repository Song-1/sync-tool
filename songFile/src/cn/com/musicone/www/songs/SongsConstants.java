/**
 * 
 */
package cn.com.musicone.www.songs;

import java.util.Calendar;

/**
 * @author Administrator
 *
 */
public class SongsConstants {
	
	public static long UPLOAD_FILE_PERIOD_TIMES = 10000L;
	
	/**
	 * 根据当前时间设置定时器的时间间隔值
	 */
	public static void setPeriodTimes(){
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);// 0-24 
		if(week == 1){
			UPLOAD_FILE_PERIOD_TIMES = 2* 60* 60* 1000L;
		}else if(week == 7 && hour >= 14){
			UPLOAD_FILE_PERIOD_TIMES = 2* 60* 60* 1000L;
		}else if(hour >= 22 || hour <= 8){
			UPLOAD_FILE_PERIOD_TIMES = 2* 60* 60* 1000L;
		}else{
			UPLOAD_FILE_PERIOD_TIMES = 5 * 60* 1000L;
		}
	}

}
