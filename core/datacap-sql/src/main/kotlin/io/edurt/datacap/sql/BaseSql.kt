package io.edurt.datacap.sql

class BaseSql : AbstractSql<BaseSql>() {
    override fun getSelf(): BaseSql {
        return this
    }
}
