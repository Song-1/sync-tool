/**
 * 
 */
package cn.com.musicone.www.oss.aliyun;


/**
 * OSS key 异常
 * @author Administrator
 */
public class OSSKeyException extends Exception {
	private static final long serialVersionUID = -2138877047022604509L;
	
	public OSSKeyException(){
		super();
	}
	public OSSKeyException(String msg){
		super(msg);
	}
	public OSSKeyException(String msg,Throwable e){
		super(msg, e);
	}
	
}
