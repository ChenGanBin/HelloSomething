package com.cyl.hello.ehcache.loaderandwriter;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

public class SampleLoaderWriter<Long, String> implements CacheLoaderWriter<Long, String> {

    @Override
    public String load(Long key) throws Exception {
        System.out.println("读取："+key);
        return null;
    }

    @Override
    public void write(Long key, String value) throws Exception {
        System.out.println("写入："+key+", 值："+value);
    }

    @Override
    public void delete(Long key) throws Exception {
        System.out.println("删除："+key);
    }


}
