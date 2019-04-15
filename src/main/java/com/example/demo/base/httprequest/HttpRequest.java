package com.example.demo.base.httprequest;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.codec.binary.Base64;

import com.example.demo.base.common.ConstantContext;
import com.example.demo.base.common.HitlibConfig;
import com.example.demo.base.common.SystemCode;
import com.example.demo.base.common.exception.ApplicationException;
import com.example.demo.utils.IOUtils;
import com.example.demo.utils.ResourceUitl;

public class HttpRequest {
//	public static final Logger LOG = LoggerFactory.getLogger(HttpRequest.class);
	public static final String HEADFIELD_CHARSET = "charset=";
	public static final String MODE_GET = "GET";
	public static final String MODE_POST = "POST";
	public static final String DEFAULT_ACCEPT = "text/html, application/xhtml+xml, */*";
	public static final String DEFAULT_CHARSET = "utf-8";
	public static final String SET_COOKIE_INDEX = "set_cookie_index";// 获取返回第几个set_cookie

	public static Map<String, Map<String, String>> cookie = new HashMap<String, Map<String, String>>();
	public static Map<String, Map<String, String>> parameter = new HashMap<String, Map<String, String>>();

	public static final int CONNECT_TIME_OUT = 300000;
	public static final int READ_TIME_OUT = 300000;

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 * @param post
	 * @return
	 * @throws ApplicationException
	 */
	public static String sendPost(String url, String post, Map<String, String> requestProperty)
			throws ApplicationException {
		requestProperty = requestProperty == null ? new HashMap<String, String>() : requestProperty;
		requestProperty.put("X-Requested-With", "XMLHttpRequest");
		requestProperty.put("Content-Type", "application/json");
		return HttpRequest.sendHttpRequestProxy(false, null, "POST", url, post, null, null, requestProperty, false);
	}

	/**
	 * 发送请求数据
	 * 
	 * @param cookieKey
	 *            响应后的cookie对应的key,每个交易有个唯一的key，能够获取cookie和设置cookie
	 * @param isUpdateCookie
	 *            响应后的cookie是否更新到全局缓存中. 更新：true, 不更新 ：false
	 * @param mode
	 *            请求方式 ： 如果是get,请用HttpRequest.MODE_GET
	 *            如果是post,请用HttpRequest.MODE_POST
	 * @param url
	 *            请求的URL
	 * @param filePath
	 *            下载的文件名
	 * @param config
	 *            配置参数对象
	 * @return 返回请求的html
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public static String sendHttpRequestProxy(Boolean isFollowRedirects, String cookieKey, String mode, String url,
			String post, ProxyInfo proxyInfo, String charset, Map<String, String> requestProperty, boolean isFile)
			throws ApplicationException {
		// 如果变量没有替换就直接退出
		if (StringUtils.isNotBlank(url)) {
			if (isFindVariable(url) || isFindVariable(post)) {
				throw new ApplicationException(SystemCode.VARIABLE_NOT_REPLACE_EXCEPTION.getCode(),
						"请求参数有 ${ 未替换，故停止请求[URL=" + url + ": post=" + post + "]");
			}
		}

		String html = null;
		post = "".equals(post) ? null : post;
		proxyInfo = proxyInfo == null ? null : (ProxyInfo) ResourceUitl.deepCopy(proxyInfo);
		int exceptionNum = 2;
		int blockNum = 2;
		int number = exceptionNum + blockNum;
		int j = 0;
		int k = 0;
		for (int i = 1; i <= number; i++) {
			try {
				html = getHtml(isFollowRedirects, cookieKey, mode, url, post, proxyInfo, charset, requestProperty,
						isFile);

				if (proxyInfo != null) {
					if (k != blockNum && isExist(cookieKey, proxyInfo, html)) {
						Thread.sleep(500);// 出现屏蔽就睡眠500ms
						k++;
						continue;
					}
				}
			} catch (ApplicationException e) {
				throw e;
			} catch (Exception e) {
				try {
					Thread.sleep(500);// 出现异常就睡眠500ms
				} catch (Exception e1) {
				}
				if (j != exceptionNum) {
					j++;
					continue;
				}
				throw new ApplicationException(SystemCode.NET_CONNECT_EXCEPTION.getCode(),
						SystemCode.NET_CONNECT_EXCEPTION.getMessage(), e);
			}
			break;
		}
		return html;
	}

	static class MyAuthenticator extends Authenticator {
		private String user = "";
		private String password = "";

		public MyAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password.toCharArray());
		}
	}

	public static boolean isExist(String cookieKey, ProxyInfo proxyInfo, String html) {
		if (proxyInfo == null || proxyInfo.getRefuseInfo() == null || html == null
				|| "".equals(proxyInfo.getRefuseInfo().trim())) {
			return false;
		}
		if (html != null && html.indexOf(proxyInfo.getRefuseInfo()) != -1) {
			return true;
		}
		return false;
	}

	public static String getHtml(Boolean isFollowRedirects, String key, String mode, String url, String post,
			ProxyInfo proxyInfo, String charset, Map<String, String> requestProperty, boolean isFile) throws Exception {

		String html = null;
		String locationUrl = null;
		// 设置重定向最大次数，不使用递归一直重定向，是防止死循环
		for (int i = 0; i < 10; i++) {
			if (StringUtils.isBlank(url)) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream ins = null;
			InputStream insError = null;
			GZIPInputStream gzip = null;
			HttpURLConnection conn = null;
			InputStreamReader inr = null;
			BufferedReader br = null;
			OutputStream bos = null;
			try {
				conn = getHttpURLConnection(isFollowRedirects, key, mode, url, post, proxyInfo, requestProperty);
				if (conn == null) {
					return null;
				}
				try {
					/**
					 * 响应请求：获取网页数据
					 */
					if (charset == null || "".equals(charset)) {
						if (conn.getHeaderFields().get("Content-Type") != null) {
							charset = getCharset(conn.getHeaderFields().get("Content-Type").toString());
						}
						if (charset == null) {
							charset = DEFAULT_CHARSET;
						}
					}
					ins = conn.getInputStream();
				} catch (Exception e) {
					if (ins != null) {
						ins.close();
					}
					throw e;
				}

				/**
				 * 响应请求：取响应后的cookie并根据条件设置cookie信息
				 */
				List<String> cookieList = new ArrayList<String>();
				String entry;
				for (int k = 1; (entry = conn.getHeaderFieldKey(k)) != null; k++) {
					if (entry.equalsIgnoreCase("set-cookie")) {
						cookieList.addAll(conn.getHeaderFields().get(entry));
					}
				}
				setCookieList(key, cookieList, requestProperty);

				// 处理重定向URL，因为自动重定向，sessionID发生变化
				locationUrl = conn.getHeaderField(ConstantContext.LOCATION_KEY);

				if (locationUrl != null && requestProperty != null) {
					requestProperty.put(ConstantContext.LOCATION_KEY, locationUrl);
				}

				if (locationUrl != null && isFollowRedirects) {
					if (locationUrl.indexOf("http") == -1) {
						String[] prexUrls = url.split("/");
						locationUrl = prexUrls[0] + "//" + prexUrls[2] + locationUrl;
					}
					mode = MODE_GET;
					url = locationUrl;
					post = null;
				} else {
					/**
					 * 响应请求：获取gzip压缩数据
					 */
					if (null != conn.getHeaderField("Content-Encoding")
							&& conn.getHeaderField("Content-Encoding").equals("gzip")) {
						gzip = new GZIPInputStream(ins);
						byte[] b = null;
						byte[] buf = new byte[1024];
						int num = -1;

						while ((num = gzip.read(buf, 0, buf.length)) != -1) {
							baos.write(buf, 0, num);
						}
						b = baos.toByteArray();
						if (isFile) {
							return new String(new Base64().encode(b), "utf-8");
						}
						return new String(b, charset).trim();
					}
					StringBuffer htmlStr = null;
					// 下载文件
					if (isFile) {
						byte[] by = IOUtils.toByteArray(ins);
						html = new String(new Base64().encode(by), "utf-8");
					} else {
						// 返回接口内容
						String line = "";
						htmlStr = new StringBuffer();
						inr = new InputStreamReader(ins, charset);
						br = new BufferedReader(inr);
						do {
							line = br.readLine();
							htmlStr.append(line == null ? "" : line).append("\n");
						} while (line != null);

						html = (htmlStr == null) ? null : htmlStr.toString();
					}
					break;
				}

			} catch (Exception e) {
				throw e;
			} finally {
				try {
					if (baos != null) {
						baos.flush();
						baos.close();
					}
					if (gzip != null) {
						gzip.close();
					}
					if (ins != null) {
						ins.close();
					}
					if (insError != null) {
						insError.close();
					}
					if (inr != null) {
						inr.close();
					}
					if (br != null) {
						br.close();
					}
					if (bos != null) {
						bos.close();
					}
					if (conn != null) {
						conn.disconnect();
					}
				} catch (Exception e) {
				}
			}
		}
		return html;

	}

	/**
	 * 返回链接
	 * 
	 * @param isFollowRedirects
	 * @param cookieKey
	 * @param mode
	 * @param url
	 * @param post
	 * @param proxyInfo
	 * @param requestProperty
	 * @return
	 * @throws Exception
	 */
	private static HttpURLConnection getHttpURLConnection(Boolean isFollowRedirects, String cookieKey, String mode,
			String url, String post, ProxyInfo proxyInfo, Map<String, String> requestProperty) throws Exception {

		if (HitlibConfig.getInstance().getBoolean("application.isTest")) {
			System.getProperties().put("proxySet", "true");
			System.getProperties().put("proxyHost", "127.0.0.1");
			System.getProperties().put("proxyPort", "8888");
		}
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("【{}】                           url ：{}", cookieKey, url);
//			LOG.debug("【{}】                         mode ：{}", cookieKey, mode);
//			LOG.debug("【{}】                         post ：{}", cookieKey, post);
//			LOG.debug("【{}】                    header ：{}", cookieKey,
//					requestProperty == null ? "" : JsonUtil.objectToJson(requestProperty));
//			LOG.debug("【{}】connectTimeout ：{}", cookieKey, CONNECT_TIME_OUT);
//			LOG.debug("【{}】       readTimeout ：{}", cookieKey, READ_TIME_OUT);
//		}
		if (url.startsWith("https://")) {
			try {
				HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
					public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
						return true;
					}
				});
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
							throws CertificateException {
					}

					public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
							throws CertificateException {
					}
				} };

				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
				return null;
			}
		}

		/**
		 * 设置请求参数
		 */
		URL serverUrl = new URL(url);
		HttpURLConnection conn = null;

		/**
		 * 判断是否需要代理IP,如果设置需要代理，但数据库没有代理IP且传入没有代理IP，那就不使用代理IP
		 */
//		if (proxyInfo != null) {
//			proxyInfo = ProxyZombie.get(proxyInfo);
//		}
		if (proxyInfo != null && proxyInfo.getIp() != null && proxyInfo.getPort() != null) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyInfo.getIp(), proxyInfo.getPort()));
			conn = (HttpURLConnection) serverUrl.openConnection(proxy);
			if (StringUtils.isNotBlank(proxyInfo.getUserName())) {
				// String authStr = proxyInfo.getUserName() + ":" + proxyInfo.getUserName();
				// conn.setRequestProperty("Authorization",
				// "Basic " + new String(Base64.encodeBase64(authStr.getBytes())));
				Authenticator.setDefault(new MyAuthenticator(proxyInfo.getUserName(), proxyInfo.getPassword()));
			}

//			if (LOG.isDebugEnabled()) {
//				LOG.debug("【切换动态代理IP：{}】【{}:{}】", cookieKey, proxyInfo.getIp(), proxyInfo.getPort());
//			}
		} else {
			conn = (HttpURLConnection) serverUrl.openConnection();
		}
		conn.setConnectTimeout(CONNECT_TIME_OUT);
		conn.setReadTimeout(READ_TIME_OUT);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod(mode);

		String cookieStr = null;
		if (requestProperty != null && requestProperty.size() > 0) {
			cookieStr = requestProperty.remove(ConstantContext.COOKIE_KEY);
			for (Map.Entry<String, String> map : requestProperty.entrySet()) {
				if (!SET_COOKIE_INDEX.equals(map.getKey())) {
					conn.addRequestProperty(map.getKey(), map.getValue());
				}
			}
		}
		/**
		 * 取当前用户的cookie,优先判断是否用配置文件的配置的cookie信息
		 */
		if (cookieStr != null && !"".equals(cookieStr)) {
			if (!cookieStr.equals("null")) {
				conn.addRequestProperty("Cookie", cookieStr);
			}
		} else if (cookieKey != null) {
			StringBuffer curentCookieInfo = new StringBuffer();
			Map<String, String> curentCookie = HttpRequest.cookie.get(cookieKey);
			if (curentCookie != null && curentCookie.size() > 0) {
				for (String key : curentCookie.keySet()) {
					curentCookieInfo.append(key + "=" + curentCookie.get(key) + ";");
				}
				conn.addRequestProperty("Cookie", curentCookieInfo.substring(0, curentCookieInfo.length() - 1));
			}
		}

		/**
		 * 开始发送请求
		 */
		if (MODE_GET.equalsIgnoreCase(mode)) {
			conn.connect();
		} else if (MODE_POST.equalsIgnoreCase(mode)) {
			conn.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
			conn.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
			conn.setUseCaches(false);// Post 请求不能使用缓存
			conn.connect();
			if (StringUtils.isNotBlank(post)) {
				conn.getOutputStream().write(post.getBytes("UTF8"));
			} else {
				conn.getOutputStream().write("".getBytes());
			}
		}

		return conn;
	}

	/**
	 * 从页面头部获取编码格式.
	 * 
	 * @param headField
	 * @return
	 */
	private static String getCharset(String headField) {
		if (headField == null) {
			return null;
		}
		int begin = 0, end = 0;
		begin = headField.indexOf(HEADFIELD_CHARSET);
		String charset = "";
		if (begin != -1) {
			end = headField.indexOf(";", begin);
			if (end == -1) {
				end = headField.indexOf("]", begin);
			}
			if (end != -1) {
				charset = headField.substring(begin + 8, end);
				return charset;
			}
		}
		return null;
	}

	protected static void setCookieList(String key, List<String> cookieList, Map<String, String> requestProperty) {
		if (key == null || cookieList == null || cookieList.size() == 0) {
			return;
		}
		// 如果配置获取第几个Set-Cookie，就返回第几个，否则就拿所有Set-Cookie信息。因为有返回两个JSESSIONID,只能指定某一个
		Map<String, String> curentCookie = HttpRequest.cookie.get(key);

		if (curentCookie == null) {
			curentCookie = new HashMap<String, String>();
			HttpRequest.cookie.put(key, curentCookie);
		}
		synchronized (curentCookie) {
			// 如果配置获取第几个Set-Cookie，就返回第几个，否则就拿所有Set-Cookie信息。因为有返回两个JSESSIONID,只能指定某一个
			if (requestProperty != null && requestProperty.get(SET_COOKIE_INDEX) != null) {
				String setCookie = cookieList.get(Integer.parseInt(requestProperty.get(SET_COOKIE_INDEX)));
				requestProperty.remove(SET_COOKIE_INDEX);
				cookieList = new ArrayList<String>();
				cookieList.add(setCookie);
			}
			for (int i = cookieList.size() - 1; i >= 0; i--) {
				String cookieInfo = cookieList.get(i);
				int index1 = cookieInfo.indexOf(";");
				int index2 = cookieInfo.indexOf("=");
				if (index1 != -1 && index2 != -1 && index2 < index1) {
					String cookieKey = cookieInfo.substring(0, index2);
					String value = cookieInfo.substring(index2 + 1, index1);
					if (value != null && !"".equals(value) && !"\"\"".equals(value)
							&& !"null".equalsIgnoreCase(value)) {
						curentCookie.put(cookieKey, value);
					}
				} else if (index1 == -1 && index2 != -1) {
					String cookieKey = cookieInfo.substring(0, index2);
					String value = cookieInfo.substring(index2 + 1);
					if (value != null && !"".equals(value) && !"\"\"".equals(value)
							&& !"null".equalsIgnoreCase(value)) {
						curentCookie.put(cookieKey, value);
					}
				}
			}
		}
	}

	/**
	 * 查找变量是否被替换。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isFindVariable(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}
		String emailPattern = ConstantContext.VAR_VALUE + "\\S*\\}";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}

}
