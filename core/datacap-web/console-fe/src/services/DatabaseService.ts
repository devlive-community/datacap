export class DatabaseService
{
  sql(type: string): string
  {
    let sql = undefined;
    switch (type.toLowerCase()) {
      case 'clickhouse':
        sql = `
          SELECT database as tableSchema, name as tableName
          FROM system.tables
          WHERE database NOT IN ('system')
          ORDER BY tableSchema, tableName
        `;
        break;
      case 'mysql':
        sql = `
          SELECT t.table_schema AS tableSchema, t.table_name AS tableName
          FROM INFORMATION_SCHEMA.TABLES t
          ORDER BY t.table_schema, t.table_name
        `;
        break;
    }
    return sql;
  }
}
