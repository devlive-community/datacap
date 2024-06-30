package io.edurt.datacap.convert.xml

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.convert.Convert
import io.edurt.datacap.convert.ConvertModule

class XmlModule : ConvertModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), Convert::class.java)
            .addBinding()
            .to(XmlConvert::class.java)
    }
}
