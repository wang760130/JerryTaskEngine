package com.jerry.taskengine.task;

import com.jerry.taskengine.context.TaskContext;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public interface Task {
	
	public void init(TaskContext taskContext);
	
	public void start() throws Exception;
	
	public void destory();
	
}
