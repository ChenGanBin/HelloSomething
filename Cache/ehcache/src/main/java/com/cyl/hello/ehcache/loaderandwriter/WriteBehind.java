package com.cyl.hello.ehcache.loaderandwriter;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.WriteBehindConfigurationBuilder;

import java.util.concurrent.TimeUnit;

public class WriteBehind {

    public static void main(String[] args) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        Cache<Long, String> writeBehindCache = cacheManager.createCache("writeBehindCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withLoaderWriter(new SampleLoaderWriter<>())
                        .add(WriteBehindConfigurationBuilder
                                .newBatchedWriteBehindConfiguration(1, TimeUnit.SECONDS, 3)
                                .queueSize(3)
                                .concurrencyLevel(1)
                                .enableCoalescing())
                        .build());

        System.out.print("键：41L, 值："+writeBehindCache.get(41L));
        writeBehindCache.put(42L, "one");
        writeBehindCache.put(43L, "two");
        writeBehindCache.put(42L, "This goes for the record");
        System.out.print("键：41L, 值："+writeBehindCache.get(42L));

        cacheManager.close();
    }
}
