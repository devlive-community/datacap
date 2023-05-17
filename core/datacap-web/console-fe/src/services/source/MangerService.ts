import {ResponseModel} from "@/model/ResponseModel";
import TemplateSqlService from "@/services/template/TemplateSqlService";
import {SqlBody} from "@/model/template/SqlBody";
import {ExecuteService} from "@/services/ExecuteService";
import {Sql} from "@/model/sql/Sql";
import {ExecuteDslBodyBuilder} from "@/model/ExecuteDslBody";
import {SqlBodyBuilder} from "@/model/builder/SqlBody";
import {SqlType} from "@/model/builder/SqlType";
import {SqlColumn, SqlColumnBuilder} from "@/model/builder/SqlColumn";
import {SqlOrder} from "@/model/builder/SqlOrder";

class ManagerService
{
  getDatabases(id: number): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: undefined,
      sourceId: id,
      templateName: 'getAllDatabase'
    };
    return TemplateSqlService.execute(configure);
  }

  /**
   * Finds all table types under the database according to the database
   * Template: FindTableTypeByDatabase
   * @param id The selected data source tag, which is stored in the database
   * @param database The query database name
   */
  findTableTypeByDatabase(id: number, database: string): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: {
        database: database
      },
      sourceId: id,
      templateName: 'FindTableTypeByDatabase'
    };
    return TemplateSqlService.execute(configure);
  }

  /**
   * Gets a collection of related data based on the specified database and data type
   * Template: FindTableByDatabaseAndType
   * @param id The selected data source tag, which is stored in the database
   * @param database The query database name
   * @param type The query table type
   */
  getTableDataByDatabaseAndType(id: number, database: string, type: string): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: {
        database: database,
        type: type
      },
      sourceId: id,
      templateName: 'FindTableByDatabaseAndType'
    };
    return TemplateSqlService.execute(configure);
  }

  /**
   * Gets the data column classification collection based on the provided database and data table
   * Template: FindColumnTypeByDatabaseAndTable
   * @param id The selected data source tag, which is stored in the database
   * @param database The query database name
   * @param table The query table name
   */
  findColumnTypeByDatabaseAndTable(id: number, database: string, table: string): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: {
        database: database,
        table: table
      },
      sourceId: id,
      templateName: 'FindColumnTypeByDatabaseAndTable'
    };
    return TemplateSqlService.execute(configure);
  }

  /**
   * Gets a collection of related data based on the specified database, table, and data type
   * Template: FindColumnByDatabaseAndTableAndType
   * @param id The selected data source tag, which is stored in the database
   * @param database The query database name
   * @param table The query table name
   * @param type The query table type
   */
  getColumnDataByDatabaseAndTableAndType(id: number, database: string, table: string, type: string): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: {
        database: database,
        table: table,
        type: type
      },
      sourceId: id,
      templateName: 'FindColumnByDatabaseAndTableAndType'
    };
    return TemplateSqlService.execute(configure);
  }

  getTables(id: number, database: string): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: {
        database: database
      },
      sourceId: id,
      templateName: 'getAllTablesFromDatabase'
    };
    return TemplateSqlService.execute(configure);
  }

  getColumns(id: number, database: string, table): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: {
        table: database + '.' + table
      },
      sourceId: id,
      templateName: 'getAllColumnsFromDatabaseAndTable'
    };
    return TemplateSqlService.execute(configure);
  }

  getData(id: number, database: string, table: string, page: number, size: number): Promise<ResponseModel>
  {
    const configure: SqlBody = {
      configure: {
        table: database + '.' + table,
        page: page,
        size: size
      },
      sourceId: id,
      templateName: 'getDataFromDatabaseAndTableLimited'
    };
    return TemplateSqlService.execute(configure);
  }

  getDataByConfigure(id: string, sql: Sql): Promise<ResponseModel>
  {
    const columns: SqlColumn[] = new Array<SqlColumn>();
    columns.push(new SqlColumnBuilder('*').build());
    // The default offset is 1, and the database index defaults to 0, which needs to be subtracted by 1
    const orders: SqlColumn[] = new Array();
    if (sql.sort) {
      sql.sort.forEach(order => {
        orders.push(new SqlColumnBuilder(order.column).setOrder(SqlOrder[order.sort]).build());
      });
    }

    const sqlBody = new SqlBodyBuilder(sql.database, sql.table)
      .setColumns(columns)
      .setOrders(orders)
      .setType(SqlType.SELECT)
      .setLimit(sql.limit)
      .setOffset(sql.offset - 1)
      .build();
    const configure = new ExecuteDslBodyBuilder(id, 'JSON')
      .setConfigure(sqlBody)
      .build();
    return new ExecuteService().executeDsl(configure);
  }
}

export default new ManagerService();
