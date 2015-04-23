/**
 * 
 */
package cn.com.musicone.www.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.StringHolder;

import com.google.gson.Gson;

/**
 * http 连接的辅助类
 * 
 * @author Administrator
 */
public class HttpClientUtil {
	private static final Logger logger = LogManager
			.getLogger(HttpClientUtil.class);
	private static final String CONVERT_STR_ENCODE = "UTF-8";

	/**
	 * 获取一个可关闭的HTTP连接客户端
	 * 
	 * @return
	 */
	public static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		return httpclient;
	}

	/**
	 * 关闭HTTP连接客户端
	 * 
	 * @param httpclient
	 */
	public static void closeHttpClient(CloseableHttpClient httpclient) {
		if (httpclient == null) {
			return;
		}
		try {
			httpclient.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			httpclient = null;
		}
	}

	/**
	 * 判断请求是否正确
	 * 
	 * @param url
	 * @param strh
	 * @return
	 */
	public static int doGet(String url, StringHolder strh) {
		if (null == strh) {
			strh = new StringHolder();
		}
		if (StringUtils.isBlank(url)) {
			strh.value = "param error";
			return -1;
		}
		HttpGet request = new HttpGet(url);
		CloseableHttpClient httpclient = getHttpClient();
		try {
			request.setHeader("Accept-Encoding", "gzip, deflate");
			request.setHeader("Accept-Language", "zh-CN");
			request.setHeader("Accept",
					"application/json, application/xml, text/html, text/*, image/*, */*");
			HttpResponse response = httpclient.execute(request);
			int stateCode = response.getStatusLine().getStatusCode();
			return stateCode;
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	/**
	 * 发送HTTP请求到指定的URL
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	public static HttpResponseData sendRequest(String url,
			HttpUriRequest request) {
		if (StringUtil.isBlank(url)) {
			return null;
		} else if (request == null) {
			return null;
		}

		HttpResponseData result = new HttpResponseData();
		CloseableHttpClient httpclient = getHttpClient();
		try {
			request.setHeader("Accept-Encoding", "gzip, deflate");
			request.setHeader("Accept-Language", "zh-CN");
			request.setHeader("Accept",
					"application/json, application/xml, text/html, text/*, image/*, */*");
			HttpResponse response = httpclient.execute(request);
			int stateCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				result.setCode(stateCode);
				result.setData(null);
				return result;
			}
			String str = convertStreamToString(entity.getContent());
			result.setCode(stateCode);
			result.setData(str);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeHttpClient(httpclient);
		}
		return null;
	}

	/**
	 * 发送HTTP GET请求
	 * 
	 * @param url
	 *            (包括参数的URL)
	 * @return
	 */
	public static HttpResponseData doGet(String url) {
		HttpGet httpgets = new HttpGet(url);
		return sendRequest(url, httpgets);
	}

	/**
	 * 发送HTTP POST请求
	 * 
	 * @param url
	 * @param map
	 *            参数集合
	 * @return
	 */
	public static HttpResponseData doPost(String url, Map<String, String> map) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (map != null) {
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, map.get(key)));
			}
		}
		return doPost(url, nvps);
	}

	/**
	 * 发送HTTP POST请求
	 * 
	 * @param url
	 * @param params
	 *            (将对象转换成JSON数据)
	 * @return
	 * @throws Exception
	 */
	public static HttpResponseData doPost(String url, Object params) {
		String paramJsons = "";
		if (params != null) {
			Gson gson = new Gson();
			paramJsons = gson.toJson(params);
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("data", paramJsons));
		return doPost(url, nvps);
	}

	/**
	 * 发送HTTP POST请求
	 * 
	 * @param url
	 * @param nvps
	 * @return
	 */
	private static HttpResponseData doPost(String url, List<NameValuePair> nvps) {
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps, CONVERT_STR_ENCODE));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return sendRequest(url, post);
	}

	/**
	 * 发送HTTP PUT请求
	 * 
	 * @param url
	 * @param map
	 *            参数集合
	 * @return
	 */
	public static HttpResponseData doPut(String url, Map<String, String> map) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (map != null) {
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, map.get(key)));
			}
		}
		return doPut(url, nvps);
	}

	/**
	 * 发送HTTP PUT请求
	 * 
	 * @param url
	 * @param params
	 *            (将对象转换成JSON数据)
	 * @return
	 * @throws Exception
	 */
	public static HttpResponseData doPut(String url, Object params) {
		String paramJsons = "";
		if (params != null) {
			Gson gson = new Gson();
			paramJsons = gson.toJson(params);
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("data", paramJsons));
		return doPut(url, nvps);
	}

	/**
	 * 发送HTTP PUT请求
	 * 
	 * @param url
	 * @param nvps
	 * @return
	 */
	private static HttpResponseData doPut(String url, List<NameValuePair> nvps) {
		HttpPut put = new HttpPut(url);
		try {
			put.setEntity(new UrlEncodedFormEntity(nvps, CONVERT_STR_ENCODE));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return sendRequest(url, put);
	}

	/**
	 * 发送HTTP DELETE请求
	 * 
	 * @param url
	 * @return
	 */
	public static HttpResponseData doDelete(String url) {
		HttpDelete delete = new HttpDelete(url);
		return sendRequest(url, delete);
	}

	/**
	 * 将HTTP请求返回的数据流转换成字符串
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				CONVERT_STR_ENCODE));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return sb.toString();
	}

	/**
	 * 根据URL下载文件到本地
	 * 
	 * @param url
	 * @param filePath
	 * @return
	 */
	public static String saveFileByURL(String url, String filePath) {
		return download(url, filePath);
	}

	/**
	 * 根据URL下载文件到本地
	 * 
	 * @param url
	 * @param filePath
	 * @return
	 */
	public static String download(String url, String filePath) {
		if (StringUtil.isBlank(url)) {
			return null;
		}
		CloseableHttpClient httpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			logger.debug("开始下载文件:::" + url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine.getStatusCode() != 200) {
				return null;
			}
			String path = filePath;
			if (StringUtil.isBlank(path)) {
				path = System.getProperty("user.dir") + File.separator + "temp"
						+ File.separator;
			}
			path += new String(url.substring(url.lastIndexOf("/") + 1));
			File file = new File(path);
			// FileDoUtil.mkDirs(file);
			logger.debug("文件下载到:::" + path);
			FileOutputStream outputStream = new FileOutputStream(file);
			InputStream inputStream = httpResponse.getEntity().getContent();
			byte b[] = new byte[1024];
			int j = 0;
			while ((j = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, j);
			}
			outputStream.flush();
			outputStream.close();
			logger.debug("文件下载完成:::" + url);
			return path;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			} finally {
				httpClient = null;
			}
		}
		return null;
	}
}
