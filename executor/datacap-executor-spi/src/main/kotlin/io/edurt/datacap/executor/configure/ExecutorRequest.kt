package io.edurt.datacap.executor.configure

import com.google.inject.Injector
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.edurt.datacap.executor.common.RunEngine
import io.edurt.datacap.executor.common.RunMode
import io.edurt.datacap.executor.common.RunWay

@SuppressFBWarnings(value = ["EI_EXPOSE_REP", "EI_EXPOSE_REP2"])
data class ExecutorRequest(
    var taskName: String,
    var userName: String,
    var input: ExecutorConfigure,
    var output: ExecutorConfigure,
    var executorHome: String? = null,
    var workHome: String? = null,
    var injector: Injector? = null,
    var timeout: Long = 600,
    var runWay: RunWay = RunWay.LOCAL,
    var runMode: RunMode = RunMode.CLIENT,
    var startScript: String? = null,
    var runEngine: RunEngine = RunEngine.SPARK
)
{
    constructor(
        workHome: String?,
        input: ExecutorConfigure,
        output: ExecutorConfigure
    ) : this("", "", input, output, null, workHome, null, 600, RunWay.LOCAL, RunMode.CLIENT)

    constructor(
        workHome: String? = null,
        executorHome: String? = null,
        taskName: String,
        userName: String,
        input: ExecutorConfigure,
        output: ExecutorConfigure,
        runMode: RunMode = RunMode.CLIENT,
        runWay: RunWay = RunWay.LOCAL
    ) : this(taskName, userName, input, output, executorHome, workHome, null, 600, runWay, runMode)

    constructor(
        workHome: String? = null,
        executorHome: String? = null,
        taskName: String,
        userName: String,
        input: ExecutorConfigure,
        output: ExecutorConfigure,
        runMode: RunMode = RunMode.CLIENT,
        runWay: RunWay = RunWay.LOCAL,
        startScript: String?
    ) : this(taskName, userName, input, output, executorHome, workHome, null, 600, runWay, runMode, startScript)

    constructor(
        workHome: String? = null,
        executorHome: String? = null,
        taskName: String,
        userName: String,
        input: ExecutorConfigure,
        output: ExecutorConfigure,
        runMode: RunMode = RunMode.CLIENT,
        runWay: RunWay = RunWay.LOCAL,
        startScript: String?,
        runEngine: RunEngine = RunEngine.SPARK
    ) : this(taskName, userName, input, output, executorHome, workHome, null, 600, runWay, runMode, startScript, runEngine)
}
