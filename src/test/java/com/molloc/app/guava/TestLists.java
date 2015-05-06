package com.molloc.app.guava;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.molloc.app.BaseTest;
import org.junit.Test;

import java.util.List;

/**
 * Created by robot on 2015/5/6.
 */
public class TestLists extends BaseTest {


    @Test
    public void testlists() {
        /**
         * 一些构造List实例的方法很简单
         * 如：newArrayList(),newLinkedList()等
         * 这里不再赘述
         */
        String str = "i love u";//测试用
        String[] strs = {"i like u", "i miss u"};//测试用
        /**
         * asList：返回一个不可变的List
         * 其中包含指定的第一个元素和附加的元素数组组成
         * 修改这个数组将反映到返回的List上
         */
        List<String> list = Lists.asList(str, strs);
        log(list);
        strs[1] = "i hate you";
        log(list);

        /**
         * transform：根据传进来的function对fromList进行相应的处理
         * 并将处理得到的结果存入到新的list对象中返回
         */
        List<String> newList = Lists.transform(list, new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input.toUpperCase();
            }
        });

        log(newList);

        /**
         * partition：根据size对传入的List进行切割，切割成符合要求的小的List
         * 并将这些小的List存入一个新的List对象中返回
         */
        List<List<String>> lists = Lists.partition(list, 2);
        log(lists);

        /**
         * charactersOf：将传进来的String或者CharSequence分割为单个的字符
         * 并存入到一个ImmutableList对象中返回
         * ImmutableList：一个高性能、不可变的、随机访问列表的实现
         */
        ImmutableList<Character> characters = Lists.charactersOf("lcyan");
        log(characters);

        /**
         * reverse：返回一个传入List内元素倒序后的List
         */
        log(Lists.reverse(list));
    }
}
