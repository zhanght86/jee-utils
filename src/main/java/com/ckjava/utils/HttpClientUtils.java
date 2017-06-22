package com.ckjava.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("deprecation")
public class HttpClientUtils {
	private static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
	
	private static int timeout = 100000;
	
	public static String put(String url, Map<String, String> params) {
		CloseableHttpClient httpclient = initWeakSSLClient();
		try {
			log.info("create http put:" + url);
			HttpPut put = putForm(url, params);
			
			return invoke(httpclient, put);
		} catch (Exception e) {
			log.error("http put has error", e);
			return null;
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * post 请求, body 是 json
	 * 
	 * @param url url String
	 * @param obj body Object 请求体
	 * @return String
	 */
	public static String post(String url, Object obj) {
		CloseableHttpClient httpclient = initWeakSSLClient();
		try {
			log.info("create http post:" + url);
			HttpPost post = postJSONForm(url, null, null, obj);

			return invoke(httpclient, post);
		} catch (Exception e) {
			log.error("http post has error", e);
			return null;
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * post 请求, body 是 json
	 * @param url String
	 * @param headers Map<String, String> 请求头
	 * @param parameters Map<String, String> 请求参数
	 * @param obj Object 请求体 
	 * @return String
	 */
	public static String post(String url, Map<String, String> headers, Map<String, String> parameters, Object obj) {
		CloseableHttpClient httpclient = initWeakSSLClient();
		try {
			log.info("create http post:" + url);
			HttpPost post = postJSONForm(url, headers, parameters, obj);

			return invoke(httpclient, post);
		} catch (Exception e) {
			log.error("http post has error", e);
			return null;
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		
	}

	/**
	 * get 请求
	 * @param url url
	 * @param params Map<String, String> 请求参数
	 * @return String
	 */
	public static String get(String url, Map<String, String> params) {
		CloseableHttpClient httpclient = initWeakSSLClient();
		try {
			// 将请求参数追加到url后面
			appendRequestParameter(url, params);

			log.info("create http get:" + url);
			HttpGet get = new HttpGet(url);
			
			return invoke(httpclient, get);
		} catch (Exception e) {
			log.error("http get has error", e);
			return null;
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
	}

	private static void appendRequestParameter(String url, Map<String, String> params) {
		if (StringUtils.isNotBlank(url) && params != null && !params.isEmpty()) {
			boolean flag = true;
			for (Iterator<Entry<String, String>> it = params.entrySet().iterator(); it.hasNext();) {
				Entry<String, String> entry = it.next();
				if (flag && url.indexOf("?") == -1) {
					url += "?";
					flag = false;
				}
				url += "&" + entry.getKey() + "=" + entry.getValue();
			}	
		}
	}

	private static String invoke(CloseableHttpClient httpclient, HttpUriRequest httprequest) {
		HttpResponse response = sendRequest(httpclient, httprequest);
		return paseResponse(response);
	}

	private static String paseResponse(HttpResponse response) {
		log.info("get response from http server..");
		HttpEntity entity = response.getEntity();

		log.info("response status: " + response.getStatusLine());
		try {
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			log.error("paseResponse has error", e);
			return null;
		}
	}

	private static HttpResponse sendRequest(HttpClient httpclient, HttpUriRequest request) {
		log.info("execute request...");
		HttpResponse response = null;

		try {
			response = httpclient.execute(request);
		} catch (Exception e) {
			log.error("sendRequest has error", e);
		}
		
		return response;
	}

	/**
	 * 
	 * @param url
	 * @param headers 
	 * @param parameters
	 * @param obj
	 * @return
	 */
	private static HttpPost postJSONForm(String url, Map<String, String> headers, Map<String, String> parameters, Object obj) {
		HttpPost httpost = new HttpPost(url);
		// 添加请求头
		if (headers != null && !headers.isEmpty()) {
			for (Iterator<Entry<String, String>> it = headers.entrySet().iterator(); it.hasNext();) {
				Entry<String, String> data = it.next();
				String key = data.getKey();
				String value = data.getValue();
				httpost.addHeader(key, value);
			}
		}
		// 添加请求参数
		appendRequestParameter(url, parameters);
		
		// 设置JSON请求体
		try {
			httpost.setEntity(new StringEntity(JSON.toJSONString(obj)));
		} catch (UnsupportedEncodingException e) {
			log.error("postForm has UnsupportedEncodingException", e);
		}

		return httpost;
	}
	
	private static HttpPut putForm(String url, Object obj) {
		HttpPut httput = new HttpPut(url);
		httput.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		
		try {
			StringEntity se = new StringEntity(JSON.toJSONString(obj), "UTF-8");
		    se.setContentType("application/json");
		    se.setContentEncoding("UTF-8");
		    
		    log.info("set utf-8 form entity to httput");
		    httput.setEntity(se);
		} catch (Exception e) {
			log.error("UnsupportedEncodingException...");
		}

		return httput;
	}
	
	private static CloseableHttpClient initWeakSSLClient() {
		HttpClientBuilder b = HttpClientBuilder.create();

	    // setup a Trust Strategy that allows all certificates.
	    //
	    SSLContext sslContext = null;
	    try {
	        sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
	            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	                return true;
	            }
	        }).build();
	    } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
	        // do nothing, has been handled outside
	    }
	    b.setSSLContext(sslContext);

	    // don't check Hostnames, either.
	    //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
	    X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

	    // here's the special part:
	    //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
	    //      -- and create a Registry, to register it.
	    //
	    SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();

	    // now, we create connection-manager using our Registry.
	    //      -- allows multi-threaded use
	    PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	    b.setConnectionManager(connMgr);

	    /**
	     * Set timeout option
	     */
	    RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(timeout);
        configBuilder.setSocketTimeout(timeout);
	    b.setDefaultRequestConfig(configBuilder.build());

	    // finally, build the HttpClient;
	    //      -- done!
	    return b.build();
	}
	
	public static void main(String[] args) {}
}
