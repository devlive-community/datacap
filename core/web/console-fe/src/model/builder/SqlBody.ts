import {SqlColumn} from "@/model/builder/SqlColumn";
import {SqlType} from "@/model/builder/SqlType";

export class SqlBody
{
  database: string;
  table: string;
  columns: Array<SqlColumn>;
  orders: Array<SqlColumn>;
  limit = 10;
  offset = 1;
  type: SqlType = SqlType.SELECT;

  constructor(builder: SqlBodyBuilder)
  {
    this.database = builder.database;
    this.table = builder.table;
    this.columns = builder.columns;
    this.orders = builder.orders;
    this.limit = builder.limit;
    this.offset = builder.offset;
    this.type = builder.type;
  }
}

export class SqlBodyBuilder
{
  private readonly _database;
  private readonly _table;
  private _columns: Array<SqlColumn>;
  private _orders: Array<SqlColumn>;
  private _limit = 10;
  private _offset = 1;
  private _type: SqlType = SqlType.SELECT;

  constructor(database: string, table: string)
  {
    this._database = database;
    this._table = table;
  }

  setColumns(columns: Array<SqlColumn>)
  {
    this._columns = columns;
    return this;
  }

  setOrders(orders: Array<SqlColumn>)
  {
    this._orders = orders;
    return this;
  }

  setLimit(limit: number)
  {
    this._limit = limit;
    return this;
  }

  setOffset(offset: number)
  {
    this._offset = offset;
    return this;
  }

  setType(type: SqlType)
  {
    this._type = type;
    return this;
  }

  get database()
  {
    return this._database;
  }

  get table()
  {
    return this._table;
  }

  get columns()
  {
    return this._columns;
  }

  get orders()
  {
    return this._orders;
  }

  get limit()
  {
    return this._limit;
  }

  get offset()
  {
    return this._offset;
  }

  get type()
  {
    return this._type;
  }

  build()
  {
    return new SqlBody(this);
  }
}
