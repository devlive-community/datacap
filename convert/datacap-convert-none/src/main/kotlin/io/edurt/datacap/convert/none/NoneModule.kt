package io.edurt.datacap.convert.none

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.convert.Convert
import io.edurt.datacap.convert.ConvertModule

class NoneModule : ConvertModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), Convert::class.java)
            .addBinding()
            .to(NoneConvert::class.java)
    }
}
