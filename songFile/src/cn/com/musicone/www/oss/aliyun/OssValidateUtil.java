/**
 * 
 */
package cn.com.musicone.www.oss.aliyun;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.musicone.www.base.utils.StringUtil;

/**
 * @author Administrator
 *
 */
public class OssValidateUtil {
	
	/**
	 * 校验oss key是否符合bucket命名规范
	 * @param bucket
	 * @throws OSSBucketException
	 */
	public static void validateOssBucket(String bucket)throws OSSBucketException{
		if(StringUtil.isBlank(bucket)){
			throw new OSSBucketException(" == oss bucket 为空  == ");
		}
		String bucketTrim = bucket.trim();
		if(!bucket.equals(bucketTrim)){
			throw new OSSBucketException(" == oss bucket 开始和结尾不允许存在空格  == ");
		}
		byte[] bytes = bucket.getBytes();
		if(bytes.length >= 64 || bytes.length <= 2){
			throw new OSSBucketException(" == oss bucket 长度必须在 3-63字节之间  == ");
		}
		Pattern p = Pattern.compile("^[a-z0-9-]+$");
		Matcher m = p.matcher(bucket);
		if(!m.find()){
			throw new OSSBucketException(" == oss bucket  只能包含小写字母,数字,短横线(-)== ");
		}
		if(bucket.startsWith("-")){
			throw new OSSBucketException(" == oss bucket 必须以小写字母或数字开头 == ");	
		}
	}
	
	/**
	 * 校验oss key是否符合Object命名规范
	 * @param key
	 * @throws OSSKeyException
	 */
	public static void validateOssKey(String key)throws OSSKeyException{
		if(StringUtil.isBlank(key)){
			throw new OSSKeyException(" == oss key 值为空  == ");
		}
		CharsetEncoder encoder =  Charset.forName("UTF-8").newEncoder();
		if(!encoder.canEncode(key)){
			throw new OSSKeyException(" == oss key 必须使用UTF-8编码  == ");
		}
		String keyTrim = key.trim();
		if(!key.equals(keyTrim)){
			throw new OSSKeyException(" == oss key 开始和结尾不允许存在空格  == ");
		}
		byte[] bytes = key.getBytes();
		if(bytes.length >= 1024){
			throw new OSSKeyException(" == oss key 长度必须在 1-1023字节之间  == ");
		}
		if(key.startsWith("/") || key.startsWith("\\")){
			throw new OSSKeyException(" == oss key 不能以'/'或者'\'字符开头  == ");
		}
	}
	public static void main(String[] args) throws OSSBucketException {
		String bucket = "sssafaf1223A552";
		validateOssBucket(bucket);
	}

}
