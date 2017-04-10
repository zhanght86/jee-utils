package com.ckjava.utils.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ckjava.utils.CommandUtils;

public class TestCommandUtils {

	@Test
	public void execTask() {
		Map<String, String> data = Collections.synchronizedMap(new HashMap<String, String>());
		data.put(CommandUtils.COMMAND_FLAG, "ipconfig");
		CommandUtils.execTask(data);
		System.out.println("result:" + data.get(CommandUtils.RESULT_TYPE_RESULT));
		System.out.println("error:"+data.get(CommandUtils.RESULT_TYPE_ERROR));
	}
}
