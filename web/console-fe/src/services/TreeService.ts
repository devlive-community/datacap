import {ResponseModel} from "@/model/ResponseModel";
import {TreeData} from "@/model/TreeData";
import {TableData} from "@/model/TableData";

export class TreeService
{
  getTree(response: ResponseModel): Array<TreeData>
  {
    const tableData = new Array();
    response.data.columns.forEach((element: TableData) => {
      let database: TreeData;
      const exists = tableData.filter(v => v.title === element.tableSchema);
      if (exists.length === 0) {
        database = {
          title: element.tableSchema,
          key: element.tableSchema,
          database: element.tableSchema,
          table: element.tableName,
          value: element.tableSchema,
          children: [],
          dataType: 'database'
        };
        tableData.push(database);
      }
      else {
        database = exists[0];
      }
      // Builder table list
      const table = this.getTableTree(element, database.children);
      if (table) {
        database.children.push(table);
      }
    })
    return tableData;
  }

  private getTableTree(element: TableData, tables: TreeData[]): TreeData | null
  {
    let table: TreeData | null = null;
    const exists = tables.filter(v => v.title === element.tableName);
    if (exists.length === 0) {
      table = {
        title: element.tableName,
        key: element.tableName,
        database: element.tableSchema,
        table: element.tableName,
        value: element.tableName,
        children: [],
        dataType: 'table'
      };
    }
    const applyTable = table ? table : exists[0];
    const column = this.getColumnTree(element, applyTable.children);
    if (column) {
      applyTable.children.push(column);
    }
    return table;
  }

  private getColumnTree(element: TableData, columns: TreeData[]): TreeData | null
  {
    const exists = columns.filter(v => v.title === element.columnName);
    if (exists.length === 0) {
      const table: TreeData = {
        title: element.columnName,
        key: element.columnName,
        database: element.tableSchema,
        table: element.tableName,
        value: element.columnName,
        children: [],
        dataType: 'column'
      };
      return table;
    }
    else {
      return null;
    }
  }
}
