package com.cyl.hello.ehcache.threadpool;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;

import java.io.File;

public class DiskStore {

    public static void main(String[] args) {
        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .using(PooledExecutionServiceConfigurationBuilder.newPooledExecutionServiceConfigurationBuilder()
                        .defaultPool("dflt", 0, 10)
                        .pool("defaultDiskPool", 1, 3)
                        .pool("cache2Pool", 2, 2)
                        .build())
                .with(new CacheManagerPersistenceConfiguration(new File("myData")))
                .withDefaultDiskStoreThreadPool("defaultDiskPool")
                .withCache("cache1",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(10, EntryUnit.ENTRIES)
                                        .disk(10L, MemoryUnit.MB)))
                .withCache("cache2",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(10, EntryUnit.ENTRIES)
                                        .disk(10L, MemoryUnit.MB))
                                .withDiskStoreThreadPool("cache2Pool", 2))
                .build(true);

        Cache<Long, String> cache1 =
                cacheManager.getCache("cache1", Long.class, String.class);
        Cache<Long, String> cache2 =
                cacheManager.getCache("cache2", Long.class, String.class);

        cacheManager.close();
    }
}
