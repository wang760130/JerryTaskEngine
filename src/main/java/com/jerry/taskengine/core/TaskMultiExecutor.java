package com.jerry.taskengine.core;

import com.jerry.taskengine.context.TaskContext;
import com.jerry.taskengine.log.ILog;
import com.jerry.taskengine.log.LogFactory;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class TaskMultiExecutor extends TaskExecutor implements Runnable {

	private static final ILog logger = LogFactory.getLogger(TaskMultiExecutor.class);

	public TaskMultiExecutor(Class<?> taskClass, TaskContext context) {
		super(taskClass, context);
	}

	@Override
	public void run() {
		try {
			super.execute();
		} catch (Exception e) {
			logger.error("AppMultiExecutor run exception", e);
		}
	}

}
