package com.jerry.taskengine.log;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class LogFactory {
	public static ILog getLogger(Class<?> cls) {
		return new Log4jLogger(cls);
	}
}
