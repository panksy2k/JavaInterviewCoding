package com.pankaj.multithreading.server.clientServer;

public class CacheCleanerTask implements Runnable {

    private final ParallelCache cache;

    public CacheCleanerTask(ParallelCache cache) {
        this.cache = cache;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            cache.cleanCache();

            try {
                Thread.sleep(2000);
                System.out.println("Now after sleep will clean the cache..");
            }
            catch(InterruptedException e) {}
        }
    }
}
