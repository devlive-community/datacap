package io.edurt.datacap.matrixone

import io.edurt.datacap.spi.Plugin

class MatrixOnePlugin : Plugin {
    override fun driver(): String {
        return "com.mysql.cj.jdbc.Driver"
    }

    override fun connectType(): String {
        return "mysql"
    }
}
