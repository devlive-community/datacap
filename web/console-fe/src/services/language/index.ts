import mysql from "@/services/language/mysql";
import clickhouse from "@/services/language/clickhouse";
import trino from "@/services/language/trino";

export default function filterLanguage(language: string) {
  switch (language.toLowerCase()) {
    case 'mysql':
      return mysql;
    case 'clickhouse':
      return clickhouse;
    case 'presto':
    case 'trino':
      return trino;
  }
}
