/**
 * 
 */
package cn.com.musicone.www.oss.aliyun;


/**
 * OSS bucket 异常
 * @author Administrator
 */
public class OSSBucketException extends Exception {
	private static final long serialVersionUID = -2138877047022604509L;
	
	public OSSBucketException(){
		super();
	}
	public OSSBucketException(String msg){
		super(msg);
	}
	public OSSBucketException(String msg,Throwable e){
		super(msg, e);
	}
	
}
