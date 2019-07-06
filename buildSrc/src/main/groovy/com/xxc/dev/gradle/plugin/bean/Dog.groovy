package com.xxc.dev.gradle.plugin.bean

class Dog {

    String name

    int age

    Dog(String name) {
        this.name = name
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
