package io.edurt.datacap.convert.csv

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.convert.Convert
import io.edurt.datacap.convert.ConvertModule

class CsvModule : ConvertModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), Convert::class.java)
            .addBinding()
            .to(CsvConvert::class.java)
    }
}
