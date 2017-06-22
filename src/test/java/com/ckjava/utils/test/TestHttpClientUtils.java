package com.ckjava.utils.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ckjava.utils.HttpClientUtils;
import com.ckjava.utils.IOUtils;

public class TestHttpClientUtils {
	public static void main(String[] args) {
		//testPostApi();
	}

	public static void testGetApi() {
		String datas = HttpClientUtils.get("https://ws.proxy.router.payment.fat45.qa.nt.ctripcorp.com/payment-route-apiservice/cache/value/get?key=payment:merchant:product:new:indexfat45&type=2", null);
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

	/**
	 * 多线程测试API性能
	 */
	public static void testApiPerformance() {
		AtomicInteger success = new AtomicInteger();
		AtomicInteger fail = new AtomicInteger();
		
		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		String originalBody = null;
		try {
			originalBody = IOUtils.getString(new FileInputStream("D:/git-workspace/jee-utils/src/test/resources/body.txt"));
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
				long consume = System.currentTimeMillis()-l;
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
		 *  consume time is 1631.631秒
			success = 200000
			fail = 0
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
			String resultStr = HttpClientUtils.post("http://10.3.6.98:8080/Dare/api/datareplay/originaldata/save", data);
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
}
