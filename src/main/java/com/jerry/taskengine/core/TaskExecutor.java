package com.jerry.taskengine.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.jerry.taskengine.context.TaskContext;
import com.jerry.taskengine.enums.TaskTypeEnum;
import com.jerry.taskengine.log.ILog;
import com.jerry.taskengine.log.LogFactory;
import com.jerry.taskengine.task.DailyTask;
import com.jerry.taskengine.task.QuartzTask;
import com.jerry.taskengine.task.Task;
import com.jerry.taskengine.utils.TaskUtil;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class TaskExecutor {

    private static final ILog logger = LogFactory.getLogger(TaskExecutor.class);
    
    private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyy-MM-dd ");
    private static final SimpleDateFormat SDF_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private Class<?> taskClass = null;
    private volatile Scheduler scheduler = null;
    private TaskContext context = null;
    
    public TaskExecutor(Class<?> taskClass, TaskContext context) {
    	this.taskClass = taskClass;
    	this.context = context;
    }
    
    private void runDailyTask(DailyTask task, Class<?> clazz) throws Exception  {
    	while(true) {
			Date current = new Date();
			String today = SDF_YMD.format(current);
			
			String startTimeStr = today + task.startTime();
			Date startTime = SDF_YMDHMS.parse(startTimeStr);
			
			String endTimeStr = today + task.endTime();
			Date endTime = SDF_YMDHMS.parse(endTimeStr);
			
			if(startTime.after(endTime)) {
				logger.error("Task start time must be greater than the end time");
				throw new RuntimeException("Task start time must be greater than the end time");
			}
			
			if(current.after(startTime) && current.before(endTime)) {
				task.start();
			} else {
				Thread.sleep(6000);
			}
		}
    }
    
    private void runQuartzTask(QuartzTask quartzTask, Class<?> task) throws SchedulerException, ParseException {
    	String taskName = task.getName();
    	String detailName = taskName.substring(taskName.lastIndexOf(".") + 1);
    	
    	Scheduler scheduler = this.getScheduler("Scheduler_" + taskName);
    	CronTrigger trigger = new CronTrigger("conTrigger_" + context.getTaskName() + "_" + detailName, null, quartzTask.quartzExprsion()); 
    	
    	JobDetail jobDetail = new JobDetail("DailyTask_detail_" + detailName, Scheduler.DEFAULT_GROUP, task);
    	scheduler.scheduleJob(jobDetail, trigger);
    	scheduler.start();
    }
   
	private synchronized Scheduler getScheduler(String schedulerName) throws SchedulerException {
		if (scheduler == null) {
			StdSchedulerFactory factory = new StdSchedulerFactory();
			Properties properties = new Properties();
			properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS,"org.quartz.simpl.SimpleThreadPool");
			properties.put("org.quartz.threadPool.threadCount",String.valueOf(context.getConfig().getQuartzThreadCount()));
			properties.put("org.quartz.scheduler.instanceName", schedulerName);
			factory.initialize(properties);
			scheduler = factory.getScheduler();
		}
		return scheduler;
	}
    
	public void execute() throws Exception {
		if(TaskUtil.getTaskType(taskClass) == TaskTypeEnum.DailyTask) {
			DailyTask task = (DailyTask) taskClass.newInstance();
			context.getTasks().add(task);
			task.init(context);
			this.runDailyTask(task, taskClass);
		} else if(TaskUtil.getTaskType(taskClass) == TaskTypeEnum.QuartzTask) {
			QuartzTask task = (QuartzTask) taskClass.newInstance();
			context.getTasks().add(task);
			task.init(context);
			this.runQuartzTask(task, taskClass);
		} else if (TaskUtil.getTaskType(taskClass) == TaskTypeEnum.Task) {
			Task task = (Task) taskClass.newInstance();
			context.getTasks().add(task);
			task.init(context);
			task.start();
		}
	}
}
