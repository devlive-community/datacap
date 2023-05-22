import {Sort} from "@/model/sql/Sort";

export class Sql
{
  database: string;
  table: string;
  columns: Array<string> = [];
  limit = 15;
  offset = 1;
  sort: Array<Sort>;
  where: string;
}
