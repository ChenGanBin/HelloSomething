package com.cyl.hello.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * 编程式配置
 */
public class EhcacheProgrammatic {

    public static void main(String[] args) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.heap(100))
                                .build())
                //true-初始化
                .build(true);

        Cache<Long, String> preConfigured
                = cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                        ResourcePoolsBuilder.heap(100)).build());

        myCache.put(1L, "myCache !");
        String value2 = myCache.get(1L);
        System.out.println("myCache: "+value2);

        cacheManager.removeCache("preConfigured");

        //这里可以使用 try-with-resources语法来关闭
        cacheManager.close();
    }
}
