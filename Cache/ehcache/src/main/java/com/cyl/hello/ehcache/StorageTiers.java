package com.cyl.hello.ehcache;

import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import java.io.File;

/**
 * 分层存储
 */
public class StorageTiers {

    public static void main(String[] args) {
        PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                //设置持久化的位置
                .with(CacheManagerBuilder.persistence(new File(".", "myData")))
                .withCache("threeTieredCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        //在堆中定义资源池
                                        .heap(10, EntryUnit.ENTRIES)
                                        //在堆外定义资源池
                                        .offheap(1, MemoryUnit.MB)
                                        //在硬盘上设置了持久化的资源池
                                        .disk(20, MemoryUnit.MB, true)
                        )
                ).build(true);

        Cache<Long, String> threeTieredCache = persistentCacheManager.getCache("threeTieredCache", Long.class, String.class);
        threeTieredCache.put(1L, "stillAvailableAfterRestart");

        persistentCacheManager.close();
    }
}
