package com.molloc.app.guava;

import com.google.common.collect.ComparisonChain;
import com.molloc.app.BaseTest;

/**
 * Created by robot on 2015/5/6.
 */
public class TestComparisonChain extends BaseTest {

    public static void main(String[] args) {
        Girl g1 = new Girl("lisa", 175.00, "nice");
        Girl g2 = new Girl("lisa", 175.00, "beauty");
        System.out.println(g1.compareTo(g2) == 0);
    }
}

class Girl implements Comparable<Girl> {
    private String name;
    private double height;
    private String face;

    public Girl(String name, double height, String face) {
        this.name = name;
        this.height = height;
        this.face = face;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    @Override
    public int compareTo(Girl girl) {
//        int c1 = name.compareTo(girl.name);
//        if (c1 != 0) {
//            System.out.println("两个girl的name不相同");
//            return c1;
//        }
//        int c2 = Double.compare(height, girl.height);
//        if (c2 != 0) {
//            System.out.println("两个girl的height不相同");
//            return c2;
//        }
//        int c3 = face.compareTo(girl.face);
//        if (c3 != 0)
//            System.out.println("两个girl的face不相同");
//        return c3;
        return ComparisonChain.start().compare(name, girl.name).compare(height, girl.height).compare(face, girl.face).result();
    }
}
