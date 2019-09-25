package com.cyl.hello.ehcache.listener;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.EventType;
import org.ehcache.impl.events.CacheEventAdapter;

/**
 * 使用事件适配器
 */
public class ListenerAdapter extends CacheEventAdapter {

    @Override
    protected void onUpdate(Object key, Object oldValue, Object newValue) {
        System.out.println("事件类型：update"
                + ", 键值："+ key
                + ", 旧值："+ oldValue
                + ", 新值："+ newValue);
    }

    @Override
    protected void onCreation(Object key, Object newValue) {
        System.out.println("事件类型：create"
                + ", 键："+ key
                + ", 值："+ newValue);
    }

    public static void main(String[] args) {
        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new ListenerAdapter(), EventType.CREATED, EventType.UPDATED)
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
