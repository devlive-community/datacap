package io.edurt.datacap.sql.builder

import io.edurt.datacap.sql.BaseSql

class ColumnBuilder {
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

        fun CREATE_COLUMN(table: String?) {
            sql().CREATE_COLUMN(table)
        }

        fun COLUMNS(columns: List<String>) {
            sql().COLUMNS(columns)
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
