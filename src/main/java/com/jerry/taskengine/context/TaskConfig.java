package com.jerry.taskengine.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class TaskConfig {

	private static TaskConfig config = null;
	// 默认配置
	private int quartzThreadCount = 1;
	// 监控端口
	private int monitorPort = 0;

	private String appName;

	private Map<String, String> callBackMap = new HashMap<String, String>();
	
	// 暂时不使用配置文件
	public synchronized static TaskConfig init() {
		return new TaskConfig();
 	}
	
	public synchronized static TaskConfig init(String fileName) {
		// 解析配置文件 
		// TODO 
		config = new TaskConfig();
		
		return config;
	}

	public static TaskConfig getConfig() {
		return config;
	}
	
	public static void setConfig(TaskConfig config) {
		TaskConfig.config = config;
	}

	public int getQuartzThreadCount() {
		return quartzThreadCount;
	}

	public void setQuartzThreadCount(int quartzThreadCount) {
		this.quartzThreadCount = quartzThreadCount;
	}

	public int getMonitorPort() {
		return monitorPort;
	}

	public void setMonitorPort(int monitorPort) {
		this.monitorPort = monitorPort;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Map<String, String> getCallBackMap() {
		return callBackMap;
	}

	public void setCallBackMap(Map<String, String> callBackMap) {
		this.callBackMap = callBackMap;
	}
}
