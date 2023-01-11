import {ResponseModel} from "@/model/ResponseModel";
import TemplateSqlService from "@/services/template/TemplateSqlService";
import {SqlBody} from "@/model/template/SqlBody";
import {ExecuteService} from "@/services/ExecuteService";
import {ExecuteModel} from "@/model/ExecuteModel";
import {Sql} from "@/model/sql/Sql";
import squel from "squel";

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
    const configure: ExecuteModel = {
      name: id,
      content: this.builderSql(sql),
      format: "JSON"
    }
    return new ExecuteService().execute(configure, null);
  }

  private builderSql(configure: Sql): string
  {
    const sql = squel.select();
    sql.from(configure.database + '.' + configure.table);
    sql.limit(configure.limit).offset(configure.offset);
    if (configure.sort) {
      if (configure.sort.sort === 'ASC') {
        sql.order(configure.sort.column, true);
      }
      else {
        sql.order(configure.sort.column, false);
      }
    }
    return sql.toString();
  }
}

export default new ManagerService();
