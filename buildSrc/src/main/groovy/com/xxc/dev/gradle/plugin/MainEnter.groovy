package com.xxc.dev.gradle.plugin

import com.android.build.gradle.AppExtension
import com.xxc.dev.gradle.plugin.bean.Animal
import com.xxc.dev.gradle.plugin.buildsrc.Student
import com.xxc.dev.gradle.plugin.factory.StudentDomainObjectFactory
import com.xxc.dev.gradle.plugin.transform.PrintTimeCast
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

class MainEnter implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("animal", Animal.class, project)
        Instantiator instantiator = ((DefaultGradle) project.gradle).services.get(Instantiator.class)
        NamedDomainObjectContainer<Student> studentContainer = project.container(Student, new StudentDomainObjectFactory(instantiator))
        project.extensions.add('team', studentContainer)
        project.afterEvaluate {
            NamedDomainObjectContainer<Student> students = project.extensions.getByName("team")
            students.each {
                println "item Student:" + it.toString()
            }
            Animal animal = project.extensions.getByType(Animal.class)
            println "animalCount:" + animal.animalCount + "  " + animal.dog

            animal.catConfig.each {
                println "cat: " + it
            }
        }
        project.getExtensions().getByType(AppExtension).registerTransform(new PrintTimeCast(project))
    }
}
