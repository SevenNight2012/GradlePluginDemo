package com.xxc.dev.gradle.plugin.bean

class Cat {

    String name

    int age

    float weight

    Cat(String name) {
        this.name = name
    }


    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                '}';
    }
}
