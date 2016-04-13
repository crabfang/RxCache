package com.cabe.lib.demo.rxcache;

/**
 *
 * Created by cabe on 16/4/13.
 */
public class Person {
    public String name;
    public int age;

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "_" + age;
    }
}
