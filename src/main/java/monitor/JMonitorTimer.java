package monitor;

import monitor.task.JMonitorTask;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class JMonitorTimer {
	  static ScheduledExecutorService scheduExec;

	  public static void load()
	  {
	    try {
			scheduExec = Executors.newScheduledThreadPool(1);
		    JMonitorTask rdtask = new JMonitorTask();
		    // 测试环境3600*24秒
		    scheduExec.scheduleWithFixedDelay(rdtask, 5L, 3600*24, TimeUnit.SECONDS);
		    System.out.println(" timer is work start ,interval : " +3600*24);	
		} catch (Exception e) {
			e.printStackTrace();
		}

	  }

	
	public static void main(String[] args) {
	}
	  

}
