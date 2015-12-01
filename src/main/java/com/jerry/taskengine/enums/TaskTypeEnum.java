package com.jerry.taskengine.enums;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public enum TaskTypeEnum {
	
	Task("Task"),
	
	DailyTask("DailyTask"),
	
	QuartzTask("QuartzTask"),
	
	Unknow("Unknow");
	
	private String type = null;
	
	TaskTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
