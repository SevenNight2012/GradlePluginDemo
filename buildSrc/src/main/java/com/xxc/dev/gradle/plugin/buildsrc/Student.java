package com.xxc.dev.gradle.plugin.buildsrc;

public class Student {

    private String name;

    private int age;

    private boolean isMan;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Student setAge(int age) {
        this.age = age;
        return this;
    }

    public boolean isMan() {
        return isMan;
    }

    public Student setIsMan(boolean man) {
        isMan = man;
        return this;
    }

    @Override
    public String toString() {
        return "Student{" + "name='" + name + '\'' + ", age=" + age + ", isMan=" + isMan + '}';
    }
}
