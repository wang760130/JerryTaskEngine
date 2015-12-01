package com.jerry.taskengine.bootstrap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.jerry.taskengine.context.TaskConfig;
import com.jerry.taskengine.context.TaskContext;
import com.jerry.taskengine.core.TaskExecutor;
import com.jerry.taskengine.core.TaskMultiExecutor;
import com.jerry.taskengine.enums.TaskTypeEnum;
import com.jerry.taskengine.log.ILog;
import com.jerry.taskengine.log.Log4jConfig;
import com.jerry.taskengine.log.LogFactory;
import com.jerry.taskengine.log.SystemPrintStream;
import com.jerry.taskengine.task.DailyTask;
import com.jerry.taskengine.task.QuartzTask;
import com.jerry.taskengine.task.QuartzTaskAdapter;
import com.jerry.taskengine.task.Task;
import com.jerry.taskengine.utils.ClassUtil;
import com.jerry.taskengine.utils.FileUtil;
import com.jerry.taskengine.utils.TaskUtil;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class Main {
	private static ILog logger = null;
	private static TaskContext context = new TaskContext();
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			throw new IllegalArgumentException("usage: -Dapp.name=<app-name> [<other-engine-config>]");
		}
		Map<String, String> argsMap = new HashMap<String, String>();

		// 是否为本地调试
		boolean isLocalTest = false;
		String taskName = null;
		
		for(int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-D")) {
                String[] aryArg = args[i].split("=");
                if (aryArg.length == 2) {
                    if (aryArg[0].equalsIgnoreCase("-Dapp.name")) {
                    	taskName = aryArg[1];
                    }
                    argsMap.put(aryArg[0].replaceFirst("-D", ""), aryArg[1]);
                }
            }
            if (args[i].equalsIgnoreCase("eclipserun")) {
                isLocalTest = true;
            }
		}

		if (isLocalTest) {
            String userDir = System.getProperty("user.dir");
            runEclipse(userDir);
        } else {
            String userDir = System.getProperty("app.dir");
            runOnline(taskName, userDir);
        }
	}
	
	
	/**
	 * 本地调试使用
	 * @param rootPath
	 * @throws IOException 
	 */
	private static void runEclipse(String rootPath) throws IOException {
		String folder = rootPath;
		String logFolder = rootPath + File.separator + "logs" + File.separator;
		String logFile = logFolder + "log.log";
		
		File logFolderFile = new File(logFolder);
		if(!logFolderFile.exists()) {
			logFolderFile.mkdirs();
		}
		Properties properties = createLog4jConfig(logFile);
		Log4jConfig.configure(properties);
		logger = LogFactory.getLogger(Main.class);
		
		// 注册监控命名控件
		TaskConfig config = TaskConfig.init();
//		TaskConfig config = TaskConfig.init(taskConfigFile);
		context.setConfig(config);
		context.setTaskName("test_"+System.currentTimeMillis());
		
		logger.info("Task start run...");
		logger.info("Begin to scan jar...");
		logger.info("folderPath : " + folder);
		List<String> jarList = FileUtil.getUniqueLibPath(folder + File.separator + "target");
		for(String jar : jarList) {
			logger.info("read jarFile path : " + jar);
			Set<Class<?>> set = getTaskClassFromJar(jar);
			if(set != null) {
				context.getClassSets().addAll(set);
			}
			
 		}
		try {
			processTask();
		} catch (Exception e) {
			logger.error("Main.processTask()",e);
		}
		registerShutDownEvent();
	}
	

	/**
	 * 部署测试环境或者生产环境使用
	 * @throws IOException 
	 */
	private static void runOnline(String taskName, String rootPath) throws IOException {
		String folder = rootPath + File.separator + "deploy" + File.separator + taskName + File.separator;
		String taskLogPath = rootPath + File.separator + "logs" + File.separator + taskName;
//		String taskClassLib = folder + "lib";
		
		// 初始化日志配置
		Properties properties = createLog4jConfig(taskLogPath);
		Log4jConfig.configure(properties);
		SystemPrintStream.redirectToLog4j();
		ILog logger = LogFactory.getLogger(Main.class);
		
		// 注册监控命名控件
		TaskConfig config = TaskConfig.init();
//		TaskConfig config = TaskConfig.init(taskConfigFolder);
		context.setConfig(config);
		context.setTaskName(taskName);
		
		logger.info("Task start run...");
		logger.info("Begin to scan jar...");
		logger.info("folderPath : " + folder);
		List<String> jarList = FileUtil.getUniqueLibPath(folder);
		for(String jar : jarList) {
			logger.info("read jarFile path : " + jar);
			Set<Class<?>> set = getTaskClassFromJar(jar);
			context.getClassSets().addAll(set);
 		}
		try {
            processTask();
        } catch (Exception e) {
        	logger.error("Main.processTask().runOnline",e);
        }
        registerShutDownEvent();
	}
	
	private static Set<Class<?>> getTaskClassFromJar(String jarPath) {
		Set<Class<?>> jarClass = null;
		try {
			Set<Class<?>> jarClasses = ClassUtil.getClassFromJar(jarPath);
			if(jarClasses != null && jarClasses.size() > 0) {
				jarClass = new LinkedHashSet<Class<?>>();
				for(Class<?> clazz : jarClasses) {
					if(ClassUtil.isAssignable(Task.class, clazz)) {
						if(clazz.getName().equals(Task.class.getName()) 
								|| clazz.getName().equals(DailyTask.class.getName()) 
								|| clazz.getName().equals(QuartzTask.class.getName())
								|| clazz.getName().equals(QuartzTaskAdapter.class.getName())) {
							continue;
						}
						jarClass.add(clazz);
					}
				} 
			}
		} catch (Exception e) {
			logger.error("getTaskClassFromJar exception", e);
		}
		return jarClass;
	}
	
	/**
	 * 执行任务
	 * @throws Exception
	 */
	private static void processTask() throws Exception {
		Set<Class<?>> classSet = context.getClassSets();
		if(classSet != null && classSet.size() > 0) {
			if(classSet.size() == 1) {
				processSingleTask(classSet.iterator().next());
			} else {
				processMultiTask(classSet);
			}
		}
		
	}
	
	/**
	 * 单任务处理
	 * @param taskClass
	 * @throws Exception 
	 */
	private static void processSingleTask(Class<?> taskClass) throws Exception {
		printProcessSingleTask(taskClass);
		TaskExecutor executor = new TaskExecutor(taskClass, context);
		executor.execute();
	}
	
	/**
	 * 多任务处理
	 * @param classSets
	 */
	private static void processMultiTask(Set<Class<?>> classSets) {
		printProcessMultiTask(classSets);
		for(Class<?> clazz : classSets) {
			Thread thread = new Thread(new TaskMultiExecutor(clazz, context)); 
			thread.start();
		}
	}
	
	
	/**
	 * 注册销毁事件
	 */
	private static void registerShutDownEvent() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Set<Task> tasks = context.getTasks();
				for (Task task : tasks) {
					task.destory();
				}
			}
		});
	}
	 
	/**
	 * 创建Log4j配置
	 * @param logFileName
	 * @return
	 */
	private static Properties createLog4jConfig(String logFileName) {
        Properties pro = new Properties();
        pro.put("log4j.rootLogger", "INFO, file");
        pro.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        pro.put("log4j.appender.stdout.Target", "System.out");
        pro.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.stdout.layout.ConversionPattern", "%m%n");
        pro.put("log4j.appender.file", "org.apache.log4j.DailyRollingFileAppender");
        pro.put("log4j.appender.file.Append", "true");
        pro.put("log4j.appender.file.Threshold", "INFO");
        pro.put("log4j.appender.file.BufferSize", "500");
        pro.put("log4j.appender.file.File", logFileName);
        pro.put("log4j.appender.file.DatePattern", "'.'yyyy-MM-dd'.log'");
        pro.put("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.file.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
        return pro;
    }
	
	private static void printProcessSingleTask(Class<?> taskClass) {
		logger.info("----------------------------------------- Register Task ------------------------------------------------------------");
		printTask(taskClass);
		logger.info("--------------------------------------------------------------------------------------------------------------------");
	}
	
	private static void printProcessMultiTask(Set<Class<?>> classSets) {
		logger.info("----------------------------------------- Register Task ------------------------------------------------------------");
		for(Class<?> clazz : classSets) {
			printTask(clazz);
		}
		logger.info("--------------------------------------------------------------------------------------------------------------------");
	}
	
	private static void printTask(Class<?> taskClass) {
		StringBuffer info = new StringBuffer();
		try {
			info.append("----  Name:");
			info.append(TaskUtil.getTaskName(taskClass));
			info.append(",  Type:");
			info.append(TaskUtil.getTaskType(taskClass).getType());
			if(TaskUtil.getTaskType(taskClass) == TaskTypeEnum.QuartzTask) {
				info.append(",  QuartzExprsion:");
				info.append(((QuartzTask) taskClass.newInstance()).quartzExprsion());
			}
			if(TaskUtil.getTaskType(taskClass) == TaskTypeEnum.DailyTask) {
				info.append(",  StartTime:");
				info.append(((DailyTask) taskClass.newInstance()).startTime());
				info.append(",  EndTime:");
				info.append(((DailyTask) taskClass.newInstance()).endTime());
			}
		} catch (InstantiationException e) {
			logger.error("printTask exception", e);
		} catch (IllegalAccessException e) {
			logger.error("printTask exception", e);
		}
		logger.info(info.toString());
	}
	
}
