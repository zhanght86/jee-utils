package com.ckjava.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpClientUtils {
	private static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
	
	public static String put(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		String body = null;

		log.info("create httpput:" + url);
		HttpPut put = putForm(url, params);
		
		body = invoke(httpclient, put);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	/**
	 * post 请求, body 是 json
	 * 
	 * @param url url
	 * @param obj body
	 * @return
	 */
	public static String post(String url, Object obj) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		log.info("create httppost:" + url);
		HttpPost post = postJSONForm(url, null, null, obj);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}
	
	public static String post(String url, Map<String, String> headers, Map<String, String> parameters, Object obj) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		log.info("create httppost:" + url);
		HttpPost post = postJSONForm(url, headers, parameters, obj);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String get(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		
		boolean flag = true;
		for (Iterator<Entry<String, String>> it = params.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> entry = it.next();
			if (flag) {
				url += "?";
				flag = false;
			}
			url += "&" + entry.getKey() + "=" + entry.getValue();
		}

		log.info("create httpget:" + url);
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httprequest) {
		HttpResponse response = sendRequest(httpclient, httprequest);
		return paseResponse(response);
	}

	private static String paseResponse(HttpResponse response) {
		log.info("get response from http server..");
		HttpEntity entity = response.getEntity();

		log.info("response status: " + response.getStatusLine());

		String body = null;
		try {
			body = EntityUtils.toString(entity);
			log.info(body);
		} catch (IOException e) {
			log.error("IOException...");
		}
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest request) {
		log.info("execute request...");
		HttpResponse response = null;

		try {
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			log.error("ClientProtocolException...");
		} catch (IOException e) {
			log.error("IOException...");
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
		if (parameters != null && !parameters.isEmpty()) {
			HttpParams params = new BasicHttpParams();
			for (Iterator<Entry<String, String>> it = parameters.entrySet().iterator(); it.hasNext();) {
				Entry<String, String> data = it.next();
				String key = data.getKey();
				String value = data.getValue();
				params.setParameter(key, value);
			}
			httpost.setParams(params);
		}
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
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException...");
		}

		return httput;
	}
	
	public static void main(String[] args) {}
}
