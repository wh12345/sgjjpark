package com.qy.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @author
 * @since 1.2.0 2016-11-28
 */
@Slf4j
@Component("testTask")
public class TestTask {

	public void test(String params){
		log.info("我是带参数的test方法，正在被执行，参数为：" + params);
		Map<String,String> map = new HashMap<String,String>();
		String[] array = params.split(",");
		for (String value:array) {
			String[] tmpArray = value.split("=");
			map.put(tmpArray[0],tmpArray[1]);
		}
	}


	public void test(){
		log.info("我是不带参数的test方法，正在被执行");
	}
}
