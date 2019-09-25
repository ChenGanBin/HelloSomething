package com.cyl.hello.ehcache;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.time.Duration;

//数据新鲜度
public class DataFreshness {

    public static void main(String[] args) {
        //设置Cache配置
        CacheConfiguration<Long, String> cacheConfiguration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Long.class, String.class, ResourcePoolsBuilder.heap(100))
                //设置Cache有效时间
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(20)))
                .build();
    }
}
