package monitor.task;

import monitor.JMonitor;

public class JMonitorTask implements Runnable{
	  public void run()
	  {
	    try
	    {
	      JMonitor.SaveRealResult();
	      System.out.println(" JMonitor data begin ------------------");
	      System.out.println(JMonitor.getRealResultJSON().toString());
	      System.out.println(" JMonitor data end --------------------");
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println(" JMonitorTask error: "+e.getMessage());
	    }
	  }

}
