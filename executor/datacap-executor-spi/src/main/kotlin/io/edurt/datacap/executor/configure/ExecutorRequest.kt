package io.edurt.datacap.executor.configure

import com.google.inject.Injector
import io.edurt.datacap.executor.common.RunMode
import io.edurt.datacap.executor.common.RunWay

data class ExecutorRequest(var taskName: String,
                           var userName: String,
                           var input: ExecutorConfigure,
                           var output: ExecutorConfigure,
                           var executorHome: String? = null,
                           var workHome: String? = null,
                           var injector: Injector? = null,
                           var timeout: Long = 600,
                           var runWay: RunWay = RunWay.LOCAL,
                           var runMode: RunMode = RunMode.CLIENT)
