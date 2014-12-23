package cn.com.musicone.www.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: BaseMD5Util
 * @Description: MD5加密计算工具类
 * @author Jelly.Liu
 * @date 2014年11月14日 下午6:01:04
 * 
 */
public class BaseMD5Util {
	/**
	 * 获取文件的MD5值
	 * 
	 * @param file
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * 
	 */
	public static String getMd5ByFile(File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return getMd5ByFile(in, file.length());
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					throw e;
				} finally {
					in = null;
				}
			}
		}

	}

	/**
	 * 获取文件的MD5值
	 * 
	 * @param in
	 *            文件输入流
	 * @param size
	 *            文件的大小
	 * @return String
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static String getMd5ByFile(InputStream in, long size) throws IOException, NoSuchAlgorithmException {
		if (in == null) {
			return null;
		} else if (size <= 0) {
			return null;
		}
		try {
			//MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, size);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = in.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
				//md5.update(byteBuffer);
			}
			BigInteger bi = new BigInteger(1, md5.digest());
			return bi.toString(16);
		} catch (IOException e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}
	}

	/**
	 * 获取字符串的MD5编码
	 * 
	 * @param str
	 * @return String
	 * 
	 */
	public static String getMd5Str(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		return DigestUtils.md5Hex(str);
	}

}
