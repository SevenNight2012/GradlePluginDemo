package com.xxc.dev.gradle.plugin.factory

import com.xxc.dev.gradle.plugin.bean.Cat
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.internal.reflect.Instantiator

class CatExtFactory implements NamedDomainObjectFactory<Cat> {

    private Instantiator instantiator

    CatExtFactory(Instantiator instantiator) {
        this.instantiator = instantiator
    }

    @Override
    Cat create(String name) {
        return instantiator.newInstance(Cat.class, name)
    }
}
