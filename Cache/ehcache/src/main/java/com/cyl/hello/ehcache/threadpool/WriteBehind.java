package com.cyl.hello.ehcache.threadpool;

import com.cyl.hello.ehcache.loaderandwriter.SampleLoaderWriter;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.*;
import org.ehcache.config.units.EntryUnit;

import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonMap;

public class WriteBehind {

    public static void main(String[] args) {
        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .using(PooledExecutionServiceConfigurationBuilder.newPooledExecutionServiceConfigurationBuilder()
                        .defaultPool("dflt", 0, 10)
                        .pool("defaultWriteBehindPool", 1, 3)
                        .pool("cache2Pool", 2, 2)
                        .build())
                .withDefaultWriteBehindThreadPool("defaultWriteBehindPool")
                .withCache("cache1",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES))
                                .withLoaderWriter(new SampleLoaderWriter<>())
                                .add(WriteBehindConfigurationBuilder
                                        .newBatchedWriteBehindConfiguration(1, TimeUnit.SECONDS, 3)
                                        .queueSize(3)
                                        .concurrencyLevel(1)))
                .withCache("cache2",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES))
                                .withLoaderWriter(new SampleLoaderWriter<>())
                                .add(WriteBehindConfigurationBuilder
                                        .newBatchedWriteBehindConfiguration(1, TimeUnit.SECONDS, 3)
                                        .useThreadPool("cache2Pool")
                                        .queueSize(3)
                                        .concurrencyLevel(2)))
                .build(true);

        Cache<Long, String> cache1 =
                cacheManager.getCache("cache1", Long.class, String.class);
        Cache<Long, String> cache2 =
                cacheManager.getCache("cache2", Long.class, String.class);

        cacheManager.close();
    }
}
