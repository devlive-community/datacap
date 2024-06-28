package io.edurt.datacap.convert.txt

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.convert.Convert
import io.edurt.datacap.convert.ConvertModule

class TxtModule : ConvertModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), Convert::class.java)
            .addBinding()
            .to(TxtConvert::class.java)
    }
}
