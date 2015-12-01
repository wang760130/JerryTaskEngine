package com.jerry.taskengine.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class Log4jLogger implements ILog {
    private transient Logger logger = null;
   
    private static final String FQCN = Log4jLogger.class.getName();

    public Log4jLogger(Class<?> cls) {
        logger = Logger.getLogger(cls);
    }

    private String getLogMsg(String msg) {
        StringBuilder sbLog = new StringBuilder();
        sbLog.append(msg);
        /* You can do something..*/
        /*
        //GaeaContext context = GaeaContext.getFromThreadLocal();
        if(context != null) {
        	sbLog.append("--");
        	sbLog.append("remoteIP:");
        	sbLog.append(context.getChannel().getRemoteIP());
        	sbLog.append("--remotePort:");
        	sbLog.append(context.getChannel().getRemotePort());
        }
        */
        return sbLog.toString();
    }

    public void fine(String message) {
        logger.log(FQCN, Level.DEBUG, getLogMsg(message), null);
    }

    public void config(String message) {
        logger.log(FQCN, Level.DEBUG, getLogMsg(message), null);
    }

    public void info(String message) {
        logger.log(FQCN, Level.INFO, getLogMsg(message), null);
    }

    public void warning(String message) {
        logger.log(FQCN, Level.WARN, getLogMsg(message), null);
    }

    // ****************************************************
    // * The methods from log4j also implemented below *
    // ****************************************************

    public void info(String message, Throwable t) {
        logger.log(FQCN, Level.INFO, getLogMsg(message), t);
    }
    
    public void debug(String message) {
        logger.log(FQCN, Level.DEBUG, getLogMsg(message), null);
    }
    
    public void debug(String message, Throwable t) {
        logger.log(FQCN, Level.DEBUG, getLogMsg(message), t);
    }
   
    public void warn(String message) {
        logger.log(FQCN, Level.WARN, getLogMsg(message), null);
    }

    public void warn(String message, Throwable t) {
        logger.log(FQCN, Level.WARN, getLogMsg(message), t);
    }

    public void error(String message) {
        logger.log(FQCN, Level.ERROR, getLogMsg(message), null);
    }

    public void error(String message, Throwable t) {
        logger.log(FQCN, Level.ERROR, getLogMsg(message), t);
    }

    public void error(Throwable e) {
        logger.log(FQCN, Level.ERROR, getLogMsg(""), e);
    }

    public void fatal(String message) {
        logger.log(FQCN, Level.FATAL, getLogMsg(message), null);
    }
    
    public void fatal(String message, Throwable t) {
        logger.log(FQCN, Level.FATAL, getLogMsg(message), t);
    }
}
