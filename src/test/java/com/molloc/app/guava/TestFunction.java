package com.molloc.app.guava;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.molloc.app.BaseTest;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by robot on 2015/5/6.
 */
public class TestFunction extends BaseTest {


    @Test
    public void testFunction() {
        Function<Date, String> function = new Function<Date, String>() {
            @Override
            public String apply(Date input) {
                return new SimpleDateFormat("yyyy-MM-dd").format(input);
            }
        };
        log(function.apply(new Date()));
    }

    static class Girl {
        int age;
        String face;

        Girl(int age, String face) {
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
    }

    @Test
    public void testPredicate() {

        Predicate<Girl> predicate = new Predicate<Girl>() {

            @Override
            public boolean apply(Girl input) {
                return input.age > 18;
            }
        };

        //有个18岁的漂亮姑娘
        Girl girl = new Girl(18, "nice");
        if (predicate.apply(girl)) {
            log("be my girl friend");
        } else {
            log("too young to love");
        }
    }

    @Test
    public void testPredicates() {
        Predicate<Girl> agePredicate = new Predicate<Girl>() {
            @Override
            public boolean apply(Girl input) {
                return input.getAge() >= 18;
            }
        };
        Predicate<Girl> facePredicate = new Predicate<Girl>() {
            @Override
            public boolean apply(Girl input) {
                return input.getFace().equals("nice");
            }
        };
        Girl girl = new Girl(18, "ugly");

        //and：用于过滤两个Predicate都为true
        Predicate<Girl> predicate = Predicates.and(agePredicate, facePredicate);
        log(predicate.apply(girl));

        //or：用于过滤其中一个Predicate为true
        predicate = Predicates.or(agePredicate, facePredicate);
        log(predicate.apply(girl));

        //not：用于将指定Predicate取反
        Predicate<Girl> noneAgePredicate = Predicates.not(agePredicate);
        predicate = Predicates.and(noneAgePredicate, facePredicate);
        log(predicate.apply(girl));

        Map<String, Girl> map = ImmutableMap.of("love", new Girl(18, "nice"), "miss", new Girl(16, "ugly"));
        Predicate<Girl> predicate1 = Predicates.and(agePredicate, facePredicate);
        Function<String, Girl> function1 = Functions.forMap(map);
        Predicate<String> stringPredicate = Predicates.compose(predicate1, function1);
        log(stringPredicate.apply("love"));//true
        log(stringPredicate.apply("miss"));//false
    }
}
