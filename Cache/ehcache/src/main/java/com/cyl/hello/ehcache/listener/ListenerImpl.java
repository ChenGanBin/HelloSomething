package com.cyl.hello.ehcache.listener;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;

/**
 * 实现{@link CacheEventListener}接口
 */
public class ListenerImpl implements CacheEventListener {

    @Override
    public void onEvent(CacheEvent event) {
        System.out.println("事件类型："+ event.getType()
                + ", 键值："+ event.getKey()
                + ", 旧值："+ event.getOldValue()
                + ", 新值："+ event.getNewValue());
    }

    public static void main(String[] args) {
        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new ListenerImpl(), EventType.CREATED, EventType.UPDATED)
                .unordered().asynchronous();

        final CacheManager manager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("foo",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))
                                .add(cacheEventListenerConfiguration)
                        //调整并发级别
                        //.withDispatcherConcurrency(10)
                ).build(true);

        final Cache<String, String> cache = manager.getCache("foo", String.class, String.class);
        cache.put("Hello", "World");
        cache.put("Hello", "Everyone");
        cache.remove("Hello");
    }
}
