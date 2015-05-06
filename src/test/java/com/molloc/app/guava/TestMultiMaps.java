package com.molloc.app.guava;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multiset;
import com.molloc.app.BaseTest;
import org.junit.Test;

import java.util.Set;

/**
 * MultiMaps: 我们也需要用到键对值的多重映射关系
 * Created by robot on 2015/5/6.
 */
public class TestMultiMaps extends BaseTest {
    /**
     * ArrayListMultimap：使用ArrayList存储键值对应的多个value值
     */
    @Test
    public void testArrayListMultimap() {
        //create()：创建具有默认初始容量的新的空ArrayListMultimap
        ArrayListMultimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("a", "1");
        multimap.put("b", "2");
        multimap.put("b", "1");
        multimap.put("a", "2");
        //获取mutlimap的键值
        Set<String> keys = multimap.keySet();
        log(keys);//[b,a]
        //mutlimap提供了keys()方法，获取Multiset<String>
        Multiset<String> multiKeys = multimap.keys();
        log(multiKeys);//[b x 2, a x 2]
        //通过multiKeys.elementSet()可以获取键值Set<String>
        //通过get()方法获取对应键所对应的value集合
        log(multimap.get("a"));//[1, 2]

        multimap.put("a", "2");
        log(multimap.get("a")); //[1, 2, 2]
    }

    /**
     * HashMultimap：基于哈希表的Multimap实现
     */
    @Test
    public void testHashMultimap() {
        //ArrayListMultimap允许重复的键值对
        ArrayListMultimap multimap = ArrayListMultimap.create();
        multimap.put("a", "1");
        multimap.put("a", "1");
        /**
         * create( Multimap<? extends K, ? extends V> multimap)
         * 构造与给定Mutimap有相同映射关系的HashMultimap实例
         */
        HashMultimap hashMultimap = HashMultimap.create(multimap);
        //如果给定Mutimap有重复的键值映射，构造后只会保留一个
        //输出：[1]，而不是[1,1]
        System.out.println(hashMultimap.get("a"));
        /**
         * 其它的如：keySet()，keys()，size()等常用方法不再赘述
         */
    }
}
