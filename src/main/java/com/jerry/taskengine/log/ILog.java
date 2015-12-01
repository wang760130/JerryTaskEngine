package com.jerry.taskengine.log;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public interface ILog {
	
    public void fine(String message);

    public void config(String message);

    public void warning(String message);

    public void debug(String message);

    public void debug(String message, Throwable t);

    public void info(String message);
    
    public void info(String message, Throwable t);

    public void warn(String message);

    public void warn(String message, Throwable t);

    public void error(String message);

    public void error(String message, Throwable t);

    public void error(Throwable e);

    public void fatal(String message);

    public void fatal(String message, Throwable t);
}
