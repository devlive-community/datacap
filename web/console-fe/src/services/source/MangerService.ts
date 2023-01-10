import {ResponseModel} from "@/model/ResponseModel";
import TemplateSqlService from "@/services/template/TemplateSqlService";
import {SqlBody} from "@/model/template/SqlBody";

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
}

export default new ManagerService();
