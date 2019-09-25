package com.cyl.hello.ehcache.listener;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;

import java.util.EnumSet;

/**
 * 运行时动态添加或移除事件监听器
 */
public class Runtime {

    public static void main(String[] args) {
        final CacheManager manager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("foo",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
                        //调整并发级别
                        //.withDispatcherConcurrency(10)
                ).build(true);

        final Cache<Long, String> cache = manager.getCache("foo", Long.class, String.class);

        System.out.println("动态添加事件监听器");
        ListenerImpl listener = new ListenerImpl();
        cache.getRuntimeConfiguration().registerCacheEventListener(listener, EventOrdering.ORDERED,
                EventFiring.ASYNCHRONOUS, EnumSet.of(EventType.CREATED, EventType.REMOVED));

        cache.put(1L, "one");
        cache.put(2L, "two");
        cache.remove(1L);
        cache.remove(2L);

        System.out.println("动态移除事件监听器");
        cache.getRuntimeConfiguration().deregisterCacheEventListener(listener);

        cache.put(1L, "one again");
        cache.remove(1L);
    }
}
