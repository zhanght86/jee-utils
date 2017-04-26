package com.ckjava.utils.test;

import org.junit.Test;

import com.ckjava.utils.CommandUtils;

public class TestCommandUtils {

	@Test
	public void execTask() {
		final StringBuffer output = new StringBuffer();
		
		Thread task = new Thread(new Runnable() {
			@Override
			public void run() {
				CommandUtils.execTask("ping www.baidu.com", output);
			}
		});
		task.start();
		while (true) {
			if (!task.isAlive()) {
				System.out.println(output.toString());
				break;
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("running");
			}
		}
		
	}
}
