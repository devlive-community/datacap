import {OrderFilter} from "@/model/OrderFilter";
import {Pagination} from "@/entity/Pagination";
import {Operator} from "@/enum/Operator";

export class TableFilter
{
  pagination?: Pagination;
  orders?: Array<OrderFilter>;
  type?: SqlType;
  columns?: Array<SqlColumn>;
  original?: Map<string, string>;
  preview?: boolean;
  value?: string;
  filter: Filter;
  newColumns?: Array<any>;
}

export enum SqlType
{
  SELECT = 'SELECT',
  UPDATE = 'UPDATE',
  DELETE = 'DELETE',
  ALTER = 'ALTER',
  SHOW = 'SHOW',
  INSERT = 'INSERT',
  TRUNCATE = 'TRUNCATE'
}

export class SqlColumn
{
  column?: string;
  value?: string;
  original?: Map<string, object>;
}

export class ColumnFilter
{
  index: number;
  column: string;
  operator: string;
  operations: Array<Operator>;
  value: any
}

export class Filter
{
  condition: string;
  filters: Array<ColumnFilter>;
}
