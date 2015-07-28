package cn.com.musicone.www.base.utils;

import org.apache.log4j.Logger;

/**
 * 
 * @ClassName: LogUtil 
 * @Description: 日志工具
 * @author Jeckey Lau
 * @date 2015年7月28日 上午10:07:22
 */
public class LogUtil {

	public static final Logger log = Logger.getLogger(LogUtil.class);
	public static final String enter_next_line = "\r\n";
	/**
	 * 记录错误
	 * @param e
	 */
	public static void printDebugException(Exception e) {
		StackTraceElement[] stackTrace = e.getStackTrace();
		System.out.println(e.getMessage());
		StringBuffer error_message = new StringBuffer(e.toString()+enter_next_line);
		for (StackTraceElement stackTraceElement : stackTrace) {
			error_message.append(stackTraceElement.toString()+enter_next_line);
		}
		log.error(error_message);
	}
	
	/**
	 * 日志debug
	 * @param message
	 */
	public static void debug(org.apache.logging.log4j.Logger logger,String message){
		logger.debug(message);
//		System.out.println(message);
	}
	
	

	/**
	 * 日志error
	 * @param message
	 */
	public static void error(org.apache.logging.log4j.Logger logger,String message){
		logger.error(message);
//		System.out.println(message);
	}
}
