package com.xxc.dev.gradle.plugin.bean


import com.xxc.dev.gradle.plugin.factory.CatExtFactory
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

class Animal {

    int animalCount
    Dog dog
    NamedDomainObjectContainer<Cat> catConfig

    Animal(Project project) {
        dog = project.extensions.create("Dog", Dog.class,"萨摩耶")
        Instantiator instantiator = ((DefaultGradle) project.gradle).services.get(Instantiator.class)
        catConfig = project.container(Cat, new CatExtFactory(instantiator))
        project.extensions.add('catConfig', catConfig)
    }


}
