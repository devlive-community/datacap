import {OrderFilter} from "@/model/OrderFilter";
import {Pagination} from "@/entity/Pagination";

export class TableFilter
{
  pagination?: Pagination;
  orders?: Array<OrderFilter>;
  type?: SqlType;
  columns?: Array<SqlColumn>;
  original?: Map<string, string>;
  preview?: boolean;
  value?: string;
}

export enum SqlType
{
  SELECT = 'SELECT',
  UPDATE = 'UPDATE',
  DELETE = 'DELETE',
  ALTER = 'ALTER',
}

export class SqlColumn
{
  column?: string;
  value?: string;
  original?: Map<string, object>;
}
