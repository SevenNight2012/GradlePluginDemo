package com.xxc.dev.gradle.plugin.factory

import com.xxc.dev.gradle.plugin.buildsrc.Student
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.internal.reflect.Instantiator

class StudentDomainObjectFactory implements NamedDomainObjectFactory<Student> {

    private Instantiator instantiator

    StudentDomainObjectFactory(Instantiator instantiator) {
        this.instantiator = instantiator
    }

    @Override
    Student create(String name) {
        return instantiator.newInstance(Student.class, name)
    }
}
