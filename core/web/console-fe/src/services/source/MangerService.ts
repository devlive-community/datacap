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
      .setOffset(sql.offset)
      .build();
    const configure = new ExecuteDslBodyBuilder(id, 'JSON')
      .setConfigure(sqlBody)
      .build();
    return new ExecuteService().executeDsl(configure);
  }
}

export default new ManagerService();
