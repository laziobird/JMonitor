package monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;



public class JMonitor {
    static final String prexCount = "_TCount";
    static final String prexTime = "_Time";
    static final String prexOnlyCount = "_Count";
    static ConcurrentHashMap<String, AtomicLong> nowResult = new ConcurrentHashMap<String, AtomicLong>();

    static Map<String, Long> lastResult = new HashMap<String, Long>();

    private static void cleanOldResult() {
        for (String key : nowResult.keySet())
            nowResult.replace(key, new AtomicLong(0L));
    }

    private static Map<String, Long> getNowResult() throws Exception {
        Map<String, Long> tmp = new HashMap<String, Long>();
        Map<String, Long> tmp2 = new HashMap<String, Long>();
        for (String key : nowResult.keySet()) {
            tmp.put(key, Long.valueOf(((AtomicLong) nowResult.get(key)).get()));
        }
        for (String key : tmp.keySet()) {
            long num = ((Long) tmp.get(key)).longValue();
            tmp2.put(key, Long.valueOf(num));
            if (key.indexOf("_Time") >= 0) {
                String stmp = key.substring(0, key.indexOf("_Time"));
                stmp = stmp + "_TCount";
                if (tmp.containsKey(stmp)) {
                    Long count = (Long) tmp.get(stmp);
                    if (count.longValue() != 0L) {
                        tmp2.put(key, Long.valueOf(num / count.longValue()));
                    }
                }
            }
        }
        return tmp2;
    }

    public static boolean SaveRealResult() {
        try {
            lastResult = getNowResult();
            cleanOldResult();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" JMonitor SaveRealResult : " + e.getCause().toString());
        }
        return false;
    }

    public static Map<String, Long> getRealResult() {
        //return lastResult;
    	try {
			return getNowResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HashMap<String,Long>();
		}
    }
    /**
     * 拿到服务器当前所以监控值，json方式展示
     * @return
     */
    public static JSONObject getRealResultJSON() {
        JSONObject jo = new JSONObject();
        for (String key : getRealResult().keySet()) {
            jo.put(key, getRealResult().get(key));
        }
        return jo;
    }

    /**
     * 监控key 加1，监控时间
     * @param key
     * @param date
     */
    public static void add(String key, Long date) {
        if ((date == null) || (StringUtils.isBlank(key)))
            return;

        String keyCount = key + "_TCount";
        String keyTime = key + "_Time";
        AtomicLong count = (AtomicLong) nowResult.get(keyCount);
        AtomicLong time = (AtomicLong) nowResult.get(keyTime);
        if ((count != null) && (time != null)) {
            count.getAndIncrement();
            time.addAndGet(date.longValue());
        } else {
            nowResult.putIfAbsent(keyCount, new AtomicLong(1L));
            nowResult.putIfAbsent(keyTime, new AtomicLong(date.longValue()));
        }
    }
    
    /**
     * set 某个key最新的监控值
     * @param key
     * @param count
     */
    public static void set(String key, Long count) {
        if ((count == null) || (StringUtils.isBlank(key)))
            return;
        String keyCount = key + "_TCount";
        nowResult.put(keyCount, new AtomicLong(count));
    }
    
    

    /**
     * 监控值加1
     * @param key
     */
    public static void add(String key) {
        if (StringUtils.isBlank(key))
            return;
        String keyCount = key + "_TCount";
        AtomicLong count = (AtomicLong) nowResult.get(keyCount);
        if (count != null)
            count.getAndIncrement();
        else
            nowResult.putIfAbsent(keyCount, new AtomicLong(1L));
    }
        

    public static void main(String[] args) {
        JMonitor jm = new JMonitor();
        List<FutureTask> list = new ArrayList<FutureTask>();
        Long l;
        for (int i = 0; i < 10000; i++) {
            l = Long.valueOf(i);
            CallableCount cn = jm.new CallableCount(l);
            FutureTask ft = new FutureTask(cn);
            list.add(ft);
            Thread td = new Thread(ft);
            td.start();
        }
        try {
            for (FutureTask one : list) {
                one.get();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("取得分支线程执行的结果后，主线程可以继续处理其他事项");
        SaveRealResult();
        System.out.println(JMonitor.getRealResultJSON());
        long j = 0L;
        for (int i = 0; i < 10000; i++) {
            j += i;
        }
        System.out.println(" real num :" + j);
    }

    class CallableCount implements Callable<Long> {
        private Long date;

        public CallableCount(Long date) {
            this.date = date;
        }

        public Long call() throws Exception {
            JMonitor.add("haha", this.date);
            System.out.println("计算的结果是:" + this.date);
            return this.date;
        }
    }

}
