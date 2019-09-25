package com.cyl.hello.ehcache

import org.junit.Test

class EhcacheTest {

//    @Test
//    public void test() {
//        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//                .withCache("preConfigured",
//                    CacheConfigurationBuilder.newCacheConfigurationBuilder(
//                        Long.class, String.class, ResourcePoolsBuilder.heap(100)).build())
//                .build(true);
//
//        Cache<Long, String> preConfigured = cacheManager.getCache("preConfigure", Long.class, String.class);
//
//        Cache<Long, String> myCache = cacheManager.createCache("myCache",
//                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100)).build());
//
//        myCache.put(1L, "da one!");
//        String value = myCache.get(1L);
//        System.out.print(value);
//        cacheManager.close();
//    }
}
