package com.jerry.taskengine.log;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class Log4jConfig {
	private static Properties staticProperties;
	private static String configFile;

	public static void configure(String configFilePath) {
		configFile = configFilePath;
		DOMConfigurator.configure(configFilePath);
	}

	public static void configure(Properties properties) {
		staticProperties = properties;
		PropertyConfigurator.configure(properties);
	}

	/**
	 * client会重定义log4j输出文件。此处需业务方主动调用。重置调用方法。
	 */
	public static void reloadConfigure() {
		if (staticProperties != null) {
			configure(staticProperties);
		}
		if (configFile != null) {
			DOMConfigurator.configure(configFile);
		}
	}
}
