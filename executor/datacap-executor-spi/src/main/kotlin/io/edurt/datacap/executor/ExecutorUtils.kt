package io.edurt.datacap.executor

import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral

object ExecutorUtils {
    @JvmStatic
    fun findOne(injector: Injector, name: String): Executor {
        return injector.getInstance(Key.get(object : TypeLiteral<Set<Executor>>() {}))
                .last { it.name() == name }
    }
}
