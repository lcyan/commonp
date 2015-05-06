package com.molloc.app.guava;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.molloc.app.BaseTest;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maps：处理Map实例的实用类
 * Created by robot on 2015/5/6.
 */
public class TestMaps extends BaseTest {

    @Test
    public void testMap() {
        /**
         * difference：返回两个给定map之间的差异。
         */
        Map<String, String> map1 = new HashMap<String, String>() {
            {
                put("a", "1");
            }
        };
        Map<String, String> map2 = new HashMap<String, String>() {
            {
                put("b", "2");
            }
        };
        Map<String, String> map3 = new HashMap<String, String>() {
            {
                put("a", "3");
            }
        };

        log(Maps.difference(map1, map2));  //not equal: only on left={a=1}: only on right={b=2}
        log(Maps.difference(map1, map3));  //not equal: value differences={a=(1, 3)}

        Set<String> set = Sets.newSet("a", "b", "c");

        Function<String, String> toUpperFunction = new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input.toUpperCase();
            }
        };
        /**
         * asMap：返回一个不可变的map
         * 键值为给定的set中的值
         * value为通过给定Function计算后的值。
         */
        log(Maps.toMap(set, toUpperFunction));//{a=A, b=B, c=C}
        /**
         * toMap：返回一个不可变的ImmutableMap实例
         * 其键值为给定keys中去除重复值后的值
         * 其值为键被计算了valueFunction后的值
         */
        List<String> keys = Lists.newArrayList("a", "b", "c", "a");
        log(Maps.toMap(keys, toUpperFunction));//  {a=A, b=B, c=C}
        /**
         * uniqueIndex：返回一个不可变的ImmutableMap实例，
         * 其value值为按照给定顺序的给定的values值
         * 键值为相应的值经过给定Function计算后的值
         */
        List<String> values = Lists.newArrayList("a", "b", "c", "d");
        /**
         * 注：这里的value值不可重复，重复的话在转换后会抛出异常：
         * IllegalArgumentException: Multiple entries with same key
         */
        log(Maps.uniqueIndex(values, toUpperFunction)); //{A=a, B=b, C=c, D=d}

        /**
         * transformValues：返回一个map映射
         * 其键值为给定fromMap的键值
         * 其value为给定formMap中value通过Function转换后的值
         */
        Map<String, Boolean> fromMap = ImmutableMap.<String, Boolean>builder().put("key", true).put("value", Boolean.FALSE).build();
        log(Maps.transformValues(fromMap, new Function<Boolean, Object>() {
            @Override
            public Object apply(Boolean input) {
                return !input;
            }
        }));//result: {key=false, value=true}

        /**
         * transformEntries：返回一个map映射
         * 其Entry为给定fromMap.Entry通过给定EntryTransformer转换后的值
         */
        Maps.EntryTransformer<String, Boolean, String> entryTransformer = new Maps.EntryTransformer<String, Boolean, String>() {
            @Override
            public String transformEntry(String key, Boolean value) {
                return (value ? key : key.toUpperCase());
            }
        };

        log(Maps.transformEntries(fromMap, entryTransformer));

        /**
         * filterKeys：返回给定unfilteredMap中的键值通过给定keyPredicate过滤后的map映射
         */
        log(Maps.filterKeys(fromMap, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.contains("y");
            }
        }));     //{key=true}

        /**
         * filterValues：返回给定unfilteredMap中的value值通过给定keyPredicate过滤后的map映射
         */
        log(Maps.filterValues(fromMap, new Predicate<Boolean>() {
            @Override
            public boolean apply(Boolean input) {
                return !input;
            }
        })); //{value=false}

        /**
         * filterEntries：返回给定unfilteredMap.Entry中的Entry值通过给定entryPredicate过滤后的map映射
         */

        log(Maps.filterEntries(fromMap, new Predicate<Map.Entry<String, Boolean>>() {
            @Override
            public boolean apply(Map.Entry<String, Boolean> input) {
                return input.getValue();
            }
        }));   //result: {key=true}
    }
}
