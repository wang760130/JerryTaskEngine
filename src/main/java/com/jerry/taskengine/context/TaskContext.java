package com.jerry.taskengine.context;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.jerry.taskengine.task.Task;

/**
 *                            _ooOoo_
 *                           o8888888o
 *                           88" . "88
 *                           (| -_- |)
 *                            O\ = /O
 *                        ____/`---'\____
 *                      .   ' \\| |// `.
 *                       / \\||| : |||// \
 *                     / _||||| -:- |||||- \
 *                       | | \\\ - /// | |
 *                     | \_| ''\---/'' | |
 *                      \ .-\__ `-` ___/-. /
 *                   ___`. .' /--.--\ `. . __
 *                ."" '< `.___\_<|>_/___.' >'"".
 *               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 *                 \ \ `-. \_ __\ /__ _/ .-` / /
 *         ======`-.____`-.___\_____/___.-`____.-'======
 *                            `=---='
 *
 *         .............................................
 *                  佛祖保佑             永无BUG
 *          佛曰:
 *                  写字楼里写字间，写字间里程序员；
 *                  程序人员写程序，又拿程序换酒钱。
 *                  酒醒只在网上坐，酒醉还来网下眠；
 *                  酒醉酒醒日复日，网上网下年复年。
 *                  但愿老死电脑间，不愿鞠躬老板前；
 *                  奔驰宝马贵者趣，公交自行程序员。
 *                  别人笑我忒疯癫，我笑自己命太贱；
 *                  不见满街漂亮妹，哪个归得程序员？
 *     ----------------------听说java继承会继承父类的所有属性→_→---------------------------
 */
public class TaskContext {
	private int monitorPort;
	private String taskName;
	private int implCount;
	private Set<Class<?>> classSets = new HashSet<Class<?>>();
	// 可能有并发写入
	private Set<Task> tasks = Collections.synchronizedSet(new LinkedHashSet<Task>());
	private TaskConfig config = null;
	
	// 暂时不用
//	private String configPath = null;
	
	// 暂时不使用Zookeeper
//	private boolean withZookeeper = false;
	
	public int getMonitorPort() {
		return monitorPort;
	}
	public void setMonitorPort(int monitorPort) {
		this.monitorPort = monitorPort;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getImplCount() {
		return implCount;
	}
	public void setImplCount(int implCount) {
		this.implCount = implCount;
	}
	public Set<Class<?>> getClassSets() {
		return classSets;
	}
	public void setClassSets(Set<Class<?>> classSets) {
		this.classSets = classSets;
	}
	public Set<Task> getTasks() {
		return tasks;
	}
	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	public TaskConfig getConfig() {
		return config;
	}
	public void setConfig(TaskConfig config) {
		this.config = config;
	}
//	public String getConfigPath() {
//		return configPath;
//	}
//	public void setConfigPath(String configPath) {
//		this.configPath = configPath;
//	}
//	public boolean isWithZookeeper() {
//		return withZookeeper;
//	}
//	public void setWithZookeeper(boolean withZookeeper) {
//		this.withZookeeper = withZookeeper;
//	}
	
}
