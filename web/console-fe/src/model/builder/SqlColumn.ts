import {SqlOrder} from "@/model/builder/SqlOrder";

export class SqlColumn
{
  column: string;
  order: SqlOrder;

  constructor(builder: SqlColumnBuilder)
  {
    this.column = builder.column;
    this.order = builder.order;
  }
}

export class SqlColumnBuilder
{
  private readonly _column: string;
  private _order: SqlOrder;

  constructor(column: string)
  {
    this._column = column;
  }

  setOrder(order: SqlOrder)
  {
    this._order = order;
    return this;
  }

  get column()
  {
    return this._column;
  }

  get order()
  {
    return this._order;
  }

  build()
  {
    return new SqlColumn(this);
  }
}
