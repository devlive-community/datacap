package io.edurt.datacap.sql.builder

import io.edurt.datacap.sql.BaseSql

class TableBuilder {
    init {
        BEGIN();
    }

    companion object {
        private val localSQL: ThreadLocal<BaseSql> = ThreadLocal<BaseSql>()

        private fun sql(): BaseSql {
            return localSQL.get()
        }

        fun BEGIN() {
            RESET()
        }

        fun RESET() {
            localSQL.set(BaseSql())
        }

        fun CREATE_TABLE(table: String?) {
            sql().CREATE_TABLE(table)
        }

        fun COLUMNS(values: List<String>) {
            sql().COLUMNS(values)
        }

        fun END() {
            sql().END()
        }

        fun SQL(): String {
            try {
                return sql().toString()
            } finally {
                RESET()
            }
        }
    }
}
