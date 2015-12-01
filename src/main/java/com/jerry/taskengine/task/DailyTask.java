package com.jerry.taskengine.task;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public interface DailyTask extends Task {
	
	/**
	 * 每日任务执行开始时间 format:  hh:mm:ss
	 * @return
	 */
	public String startTime();
	
	/**
	 * 每日任务执行结束时间 format:  hh:mm:ss
	 * @return
	 */
	public String endTime();
}
