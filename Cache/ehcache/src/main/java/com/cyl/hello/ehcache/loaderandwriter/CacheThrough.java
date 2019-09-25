package com.cyl.hello.ehcache.loaderandwriter;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class CacheThrough {

    public static void main(String[] args) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        Cache<Long, String> writeThroughCache = cacheManager.createCache("writeThroughCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withLoaderWriter(new SampleLoaderWriter<>())
                        .build());

        System.out.println("键：41L, 值："+writeThroughCache.get(41L));
        writeThroughCache.put(42L, "one");
        System.out.println("键：42L, 值："+writeThroughCache.get(42L));

        cacheManager.close();
    }
}
