package com.cyl.hello.cache.localcache;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存，基于ConcurrentHashMap实现
 */
public class LocalCache {

    /** 默认有效时长，单位：秒 */
    private static final int DEFAULE_TIMEOUT = 3600;

    private static final long SECOND_TIME = 1000;

    private static final Map<String, Object> map;

    private static final Timer timer;

    static {
        timer = new Timer();
        map = new ConcurrentHashMap<>();
    }

    private LocalCache(){}

    static class CleanWorkerTask extends TimerTask {

        private String key;

        public CleanWorkerTask(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            LocalCache.remove(key);
        }
    }

    public static void put(String key, Object value) {
        map.put(key, value);
        timer.schedule(new CleanWorkerTask(key), DEFAULE_TIMEOUT);
    }

    public static void put(String key, Object value, int timeout) {
        map.put(key, value);
        timer.schedule(new CleanWorkerTask(key), timeout * SECOND_TIME);
    }

    public static void put(String key, Object value, Date expireTime) {
        map.put(key, value);
        timer.schedule(new CleanWorkerTask(key), expireTime);
    }

    public static void putAll(Map<String, Object> m) {
        map.putAll(m);

        for(String key : m.keySet()) {
            timer.schedule(new CleanWorkerTask(key), DEFAULE_TIMEOUT);
        }
    }

    public static void putAll(Map<String, Object> m, int timeout) {
        map.putAll(m);

        for (String key : m.keySet()) {
            timer.schedule(new CleanWorkerTask(key), timeout * SECOND_TIME);
        }
    }

    public static void putAll(Map<String, Object> m, Date expireTime) {
        map.putAll(m);

        for(String key : m.keySet()) {
            timer.schedule(new CleanWorkerTask(key), expireTime);
        }
    }

    public static Object get(String key) {
        return map.get(key);
    }

    public static boolean containKey(String key) {
        return map.containsKey(key);
    }

    public static void remove(String key) {
        map.remove(key);
    }

    public static void remove(Object o) {
        map.remove(o);
    }

    public static int size() {
        return map.size();
    }

    public static void clear() {
        if(size() > 0) {
            map.clear();
        }
        timer.cancel();
    }
}
