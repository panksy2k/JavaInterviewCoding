package com.pankaj.multithreading.server.clientServer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelCache {
    private final Map<String, CacheItem<String, String>> cacheDS;
    private final Thread cleanTask;

    private static final int MAX_TIME_TO_LIVE_MILLISECONDS = 10 * 1000;

    public ParallelCache() {
        this.cacheDS = new ConcurrentHashMap<>();
        cleanTask = new Thread(() -> new CacheCleanerTask(this));

        cleanTask.start();
    }

    public void addToCache(String key, String value) {
        System.out.println("Adding into cache");
        cacheDS.put(key, new CacheItem<>(key, value));
    }

    public String getFromCache(String key) {
        CacheItem<String, String> cachedItem = cacheDS.get(key);
        if (null != cachedItem) {
            cachedItem.setAccessDate(LocalDate.now());
            System.out.println("Fetching from cache");
            return cachedItem.getValue();
        }

        System.out.println("Not in cache");
        return "";
    }

        public void cleanCache() {
        Iterator<CacheItem<String, String>> iterator = cacheDS.values().iterator();
        while(iterator.hasNext()) {
            CacheItem cacheItem = iterator.next();
            if(cacheItem != null && cacheItem.getAccessDate() != null && MAX_TIME_TO_LIVE_MILLISECONDS < Duration.between(LocalDate.now(), cacheItem.getAccessDate()).getSeconds() * 1000) {
                System.out.println("Removing an item from cache");
                iterator.remove();
            }
        }
    }

    public void shutDownCache() {
        cleanTask.interrupt();
    }
}
