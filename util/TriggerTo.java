package com.bjsasc.twc.brace.util;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bjsasc.platform.quartz.PTStatefulJob;
/**
 * 调度任务执行
 * @author 陈建立
 * 2015-12-18
 */
public class TriggerTo implements PTStatefulJob{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try{
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
