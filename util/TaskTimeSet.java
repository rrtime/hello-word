package com.bjsasc.twc.brace.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;

import com.bjsasc.platform.quartz.PTJobDataMap;
import com.bjsasc.platform.quartz.PTJobServiceHelper;
import com.bjsasc.platform.quartz.PlatformJobService;
import com.bjsasc.platform.quartz.PlatformJobServiceUtil;

/**
 * 定时任务设置
 * @author 陈建立
 * 2015-12-18
 */
public class TaskTimeSet {
	/**
	 * 增加定时任务
	 * @param timedata	timedata数据库保存定时任务时间对象
	 * @param IHH	定时任务开始小时
	 * @param Imm	定时任务开始分钟
	 * @param Iss	定时任务开始秒
	 */
	@SuppressWarnings("rawtypes")
	public static void addTimer(Map timedata,int IHH,int Imm ,int Iss){
		PTJobServiceHelper helper = new PTJobServiceHelper();
		try {
	        PTJobDataMap jobDataMap = new PTJobDataMap();
	        // JOBGROUPNAME 定时任务所属组的名称
	        String innerid = CommonUtilTool.DelNull(timedata.get("INNERID"));
	        helper.prepareJob(innerid, null,TriggerTo.class, jobDataMap);
	        Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, IHH);
			c.set(Calendar.MINUTE, Imm);
			c.set(Calendar.SECOND, Iss);
			Date dt = c.getTime();
	        helper.prepareSimpleTrigger(innerid,null,innerid,null,dt,null,SimpleTrigger.REPEAT_INDEFINITELY,3600L * 1000L*24L);
	        PlatformJobService js = PlatformJobServiceUtil.getPlatformJobService();
            JobDetail jd = js.getScheduler().getJobDetail(innerid, null);
            if (null == jd) {
                helper.scheduleJob();
            }
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * 更新定时任务
	 * @param timedata	timedata数据库保存定时任务时间对象
	 * @param IHH	定时任务开始小时
	 * @param Imm	定时任务开始分钟
	 * @param Iss	定时任务开始秒
	 */
	@SuppressWarnings("rawtypes")
	public static void updateTimer(Map timedata,int IHH,int Imm ,int Iss){
		PTJobServiceHelper helper = new PTJobServiceHelper();
		try {
	        PTJobDataMap jobDataMap = new PTJobDataMap();
	        // JOBGROUPNAME 定时任务所属组的名称
	        String innerid = CommonUtilTool.DelNull(timedata.get("INNERID"));
	        helper.prepareJob(innerid, null,TriggerTo.class, jobDataMap);
	        helper.deleteJob(innerid, null);
	        Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, IHH);
			c.set(Calendar.MINUTE, Imm);
			c.set(Calendar.SECOND, Iss);
			Date dt = c.getTime();
	        helper.prepareSimpleTrigger(innerid,null,innerid,null,dt,null,SimpleTrigger.REPEAT_INDEFINITELY,3600L * 1000L*24L);
	        helper.scheduleJob();
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * 删除定时任务
	 * @param timedata	timedata数据库保存定时任务时间对象
	 */
	@SuppressWarnings("rawtypes")
	public static void delTimer(Map timedata){
		PTJobServiceHelper helper = new PTJobServiceHelper();
		try {
			 String innerid = CommonUtilTool.DelNull(timedata.get("INNERID"));
			 helper.deleteJob(innerid, null);
		} catch (Exception e){
	    	e.printStackTrace();
		}
	}
	
}
