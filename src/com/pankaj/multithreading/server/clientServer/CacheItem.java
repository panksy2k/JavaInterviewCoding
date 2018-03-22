package com.pankaj.multithreading.server.clientServer;

import java.time.LocalDate;

public class CacheItem<K, V> {

    private final K key;
    private final V value;

    private LocalDate accessDate;

    public CacheItem(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public LocalDate getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(LocalDate accessDate) {
        this.accessDate = accessDate;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
