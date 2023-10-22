import {OrderFilter} from "@/model/OrderFilter";
import {Pagination} from "@/entity/Pagination";

export class TableFilter
{
  pagination: Pagination;
  orders: Array<OrderFilter>;
}
