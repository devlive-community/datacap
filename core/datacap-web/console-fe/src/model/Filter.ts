import {Order} from "@/model/Order";

export class Filter
{
  page = 1;
  size = 10;
  orders: Array<Order>;
}
