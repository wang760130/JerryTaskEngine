package com.jerry.taskengine.utils;

import com.jerry.taskengine.enums.TaskTypeEnum;
import com.jerry.taskengine.task.DailyTask;
import com.jerry.taskengine.task.QuartzTask;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class TaskUtil {
	
	public static TaskTypeEnum getTaskType(Class<?> taskClass) {
		TaskTypeEnum type = TaskTypeEnum.Unknow;
		if(ClassUtil.isAssignable(DailyTask.class, taskClass)) {
			type = TaskTypeEnum.DailyTask;
		} else if(ClassUtil.isAssignable(QuartzTask.class, taskClass)) {
			type = TaskTypeEnum.QuartzTask;
		} else {
			type = TaskTypeEnum.Task;
		}
		return type;
	}
	
	public static String getTaskName(Class<?> taskClass) {
		String name = taskClass.getName();
		return name.substring(name.lastIndexOf(".") + 1, name.length()); 
	}
	
}
