package com.molloc.app.guava;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.molloc.app.BaseTest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 链式的编程,操作Iterable
 * Created by robot on 2015/5/6.
 */
public class TestFluentIterable extends BaseTest {

    private List<Boy> boys;

    @Before
    public void setup() {
        Boy boy1 = new Boy(17, "nice");
        Boy boy2 = new Boy(18, "not so nice");
        Boy boy3 = new Boy(19, "nice");
        Boy boy4 = new Boy(20, "not so nice");
        //这里使用Lists.newArrayList添加对象，避免多次调用list.add方法，下篇会有说明
        boys = Lists.newArrayList(boy1, boy2, boy3, boy4);
    }


    @Test
    public void testFluentIterable() {
        /**
         * from方法：用于包装Iterable接口，返回FluentIterable实例
         * filter方法：用于过滤集合中元素，返回过滤后的集合
         */
        Iterable<Boy> iterable = FluentIterable.from(boys).filter(new Predicate<Boy>() {
            @Override
            public boolean apply(Boy input) {
                return "nice".equals(input.face);
            }
        });

        for (Boy boy : iterable) {
            log(boy);
        }

        /**
         * transform方法：用于根据指定Function转换集合元素，返回转换后的集合
         */
        Iterable<String> fluentIterable = FluentIterable.from(boys).transform(new Function<Boy, String>() {
            @Override
            public String apply(Boy input) {
                return Joiner.on(',').join(input.getAge(), input.getFace());
            }
        });

        for (String boy : fluentIterable) {
            log(boy);
        }

    }

}


class Boy {
    int age;
    String face;

    Boy(int age, String face) {
        this.age = age;
        this.face = face;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}