package io.edurt.datacap.convert.json

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.convert.Convert
import io.edurt.datacap.convert.ConvertModule

class JsonModule : ConvertModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), Convert::class.java)
            .addBinding()
            .to(JsonConvert::class.java)
    }
}
