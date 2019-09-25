package com.cyl.hello.ehcache;

import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

/**
 * XML式配置
 */
public class EhcacheXml {

    public static void main(String[] args) {
        URL url = EhcacheXml.class.getClass().getResource("ehcache.xml");
        Configuration xmlConfig = new XmlConfiguration(url);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
    }
}
