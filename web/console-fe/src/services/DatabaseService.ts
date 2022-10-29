export class DatabaseService
{
  sql(type: string): string
  {
    let sql = '';
    switch (type.toLowerCase()) {
      case 'clickhouse':
        sql = `
          SELECT database as tableSchema, table as tableName, name as columnName, type as dataType
          FROM system.columns
          WHERE database NOT IN ('system')
          ORDER BY tableSchema, tableName
        `;
        break;
      case 'mysql':
        sql = `
          SELECT t.table_schema AS tableSchema, t.table_name AS tableName, c.column_name AS columnName, c.data_type AS dataType
          FROM INFORMATION_SCHEMA.TABLES t
                 JOIN INFORMATION_SCHEMA.COLUMNS c ON t.table_schema = c.table_schema AND t.table_name = c.table_name
          ORDER BY t.table_schema, t.table_name, c.ordinal_position
        `;
        break;
    }
    return sql;
  }
}
