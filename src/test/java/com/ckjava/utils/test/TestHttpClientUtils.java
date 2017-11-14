package com.ckjava.utils.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ckjava.utils.CollectionUtils;
import com.ckjava.utils.HttpClientUtils;
import com.ckjava.utils.IOUtils;

public class TestHttpClientUtils {
	public static void main(String[] args) {
		// testPostApi();
		//testPostApi2();
		//testPostApi3();
		testDeleteApi();
	}
	
	public static void testDeleteApi() {
		String datas = HttpClientUtils.delete("http://localhost:9087/legends/api/job/4", CollectionUtils.asHashMap(new String[]{ "offlineTicket", "username" }, new String[]{ "uiauto", "chen_k" }), null);
		System.out.println(datas);
	}

	public static void testGetApi() {
		String datas = HttpClientUtils.get(
				"https://ws.proxy.router.payment.fat45.qa.nt.ctripcorp.com/payment-route-apiservice/cache/value/get?key=payment:merchant:product:new:indexfat45&type=2",
				null, null);
		System.out.println(datas);
	}

	public static void getClassPathFile() {
		String originalBody = null;
		try {
			originalBody = IOUtils.getString(TestHttpClientUtils.class.getResourceAsStream("/body.txt")); // 从classpath下获取文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(originalBody);
	}

	public static void testPostApi() {
		String url = "http://ws.ebank.payment.fat18.qa.nt.ctripcorp.com/PaymentPasswordServiceAPI/GetUserPWDInfo";
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json;charset=utf-8");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("CustomerID", "M01230808");
		Map<String, String> body = new HashMap<>();
		body.put("CustomerID", "M01230808");
		String resultStr = HttpClientUtils.post(url, headers, parameters, body);
		System.out.println(resultStr);
	}

	public static void testPostApi2() {
		String url = "http://ws.ebank.payment.fat18.qa.nt.ctripcorp.com/PaymentPasswordServiceAPI/GetUserPWDInfo";
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json;charset=utf-8");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("CustomerID", "M01230808");

		String body = "{     \"CustomerID\":\"M01230808\" }";
		Object obj = JSON.parse(body);
		String resultStr = HttpClientUtils.post(url, headers, parameters, obj);
		System.out.println(resultStr);
	}
	
	public static void testPostApi3() {
		String url = "http://10.32.74.44:9087/legends/api/job";

		Map<String, Object> jobData = new HashMap<String, Object>();
		jobData.put("id", "6");
		jobData.put("name", "本地测试-1");
		jobData.put("jobGroup", "UI自动化");
		jobData.put("type", "REPEAT");
		jobData.put("cron", "0 48 12 * * ?");
		jobData.put("urls", "http://10.32.74.44/uiauto/job");
		jobData.put("classPath", "com.ctrip.uiauto.webtool.job.RunCaseJob");
		jobData.put("invokePolicy", "PRIORITY");
		jobData.put("isClose", "no");
		jobData.put("checkFinishTime", "00:00");
		jobData.put("param", "10");
		
		String resultStr = HttpClientUtils.post(url, CollectionUtils.asHashMap(new String[]{ "offlineTicket", "username" }, new String[]{ "uiauto", "chen_k" }), null, jobData);
		System.out.println(resultStr);
	}

	/**
	 * 多线程测试API性能
	 */
	public static void testApiPerformance() {
		AtomicInteger success = new AtomicInteger();
		AtomicInteger fail = new AtomicInteger();

		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		String originalBody = null;
		try {
			originalBody = IOUtils
					.getString(new FileInputStream("D:/git-workspace/jee-utils/src/test/resources/body.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map<String, String>> dataList = new ArrayList<>();
		Map<String, String> data = new HashMap<String, String>();
		data.put("name", "test_api_save_batch_6");
		data.put("originalBody", originalBody);
		dataList.add(data);

		long l = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			executorService.submit(new ApiSaveWorker(dataList, success, fail));
		}

		while (true) {
			if (executorService.getActiveCount() == 0) {
				long consume = System.currentTimeMillis() - l;
				System.out.println(consume);
				System.out.println("consume time is " + (consume) / (1000D) + "秒");
				System.out.println("success = " + success.get());
				System.out.println("fail = " + fail.get());
				executorService.shutdown();
				break;
			}
		}
		/**
		 * 200000
		 * 
		 * consume time is 1631.631秒 success = 200000 fail = 0
		 */
	}

	public static class ApiSaveWorker implements Runnable {

		private Object data;
		private AtomicInteger success;
		private AtomicInteger fail;

		public ApiSaveWorker(Object data, AtomicInteger success, AtomicInteger fail) {
			super();
			this.data = data;
			this.success = success;
			this.fail = fail;
		}

		@Override
		public void run() {
			String resultStr = HttpClientUtils.post("http://10.3.6.98:8080/Dare/api/datareplay/originaldata/save",
					data);
			JSONObject jsonobj = JSON.parseObject(resultStr);
			if (jsonobj.getString("code").equals("200")) {
				success.getAndAdd(1);
				System.out.println("success");
			} else {
				fail.getAndAdd(1);
				System.out.println("fail");
			}
		}

	}

	//@Test
	public void testContentType() {
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/x-www-form-urlencoded");

			Map<String, String> params = new HashMap<String, String>();
			params.put(URLEncoder.encode("api.token", "UTF-8"),
					URLEncoder.encode("api-ipsbw4rx6cigtt7tcdqeiihdv4sz", "UTF-8"));

			params.put(URLEncoder.encode("task_id", "UTF-8"), URLEncoder.encode("8", "UTF-8"));

			String result = HttpClientUtils
					.get("http://finance.phabricator.dev.qa.nt.ctripcorp.com/api/maniphest.search", headers, params);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void testPostUrlEncodeForm() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		Map<String, String> params = new HashMap<String, String>();
		params.put("api.token", "api-ipsbw4rx6cigtt7tcdqeiihdv4sz");

		List<NameValuePair> formParams = new ArrayList<>();
		formParams.add(new BasicNameValuePair("params[task_id]", "445"));
		formParams.add(new BasicNameValuePair("output", "json"));
		//formParams.add(new BasicNameValuePair("__form__", "1"));

		String result = HttpClientUtils.postUrlEncodeForm("http://finance.phabricator.dev.qa.nt.ctripcorp.com/api/maniphest.info", headers, params, formParams);
		System.out.println(result);
	}
	
	//@Test
	public void testCurl() {
		try {
			Response res = Request.Post("http://finance.phabricator.dev.qa.nt.ctripcorp.com/api/maniphest.info")
					.bodyForm(Form.form()
					.add("task_id", "445")
					.add("api.token", "api-ipsbw4rx6cigtt7tcdqeiihdv4sz")
					.build())
					.execute();
			Content content = res.returnContent();
			System.out.println(content.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testPhabricatorAPI() {
		try {
			Response res = Request.Post("http://test.pha.com/api/maniphest.search")
					.bodyForm(Form.form()
					.add("api.token", "api-6mlsh56cb5uexqbxgpnvah6djhmc")
					.add("queryKey", "all")
					.add("constraints[statuses][0]", "open")
					.add("attachments[subscribers]", "1")
					.add("order[0]", "id")
					.build())
					.execute();
			Content content = res.returnContent();
			System.out.println(content.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
