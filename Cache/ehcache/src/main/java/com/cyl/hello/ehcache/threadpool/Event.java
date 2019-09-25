package com.cyl.hello.ehcache.threadpool;

import com.cyl.hello.ehcache.listener.ListenerAdapter;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.*;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.event.EventType;

public class Event {

    public static void main(String[] args) {
        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .using(PooledExecutionServiceConfigurationBuilder.newPooledExecutionServiceConfigurationBuilder()
                        .pool("defaultEventPool", 1, 3)
                        .pool("cache2Pool", 2, 2)
                        .build())
                .withDefaultEventListenersThreadPool("defaultEventPool")
                .withCache("cache1",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES))
                                .add(CacheEventListenerConfigurationBuilder
                                        .newEventListenerConfiguration(new ListenerAdapter(), EventType.CREATED, EventType.UPDATED)))
                .withCache("cache2",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES))
                                .add(CacheEventListenerConfigurationBuilder
                                        .newEventListenerConfiguration(new ListenerAdapter(), EventType.CREATED, EventType.UPDATED))
                                .withEventListenersThreadPool("cache2Pool"))
                .build(true);

        Cache<Long, String> cache1 =
                cacheManager.getCache("cache1", Long.class, String.class);
        Cache<Long, String> cache2 =
                cacheManager.getCache("cache2", Long.class, String.class);

        cacheManager.close();
    }
}
