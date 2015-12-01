package com.jerry.taskengine.task;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public abstract class QuartzTaskAdapter implements Task, StatefulJob {

    private static final Logger logger = Logger.getLogger(QuartzTaskAdapter.class);

  
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            start();
        } catch (Throwable e) {
            logger.error("QuartzTaskAdapter execute",e);
        }
    }
}
