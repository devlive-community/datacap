export class Pagination
{
  current = 1;
  total = 0;
  size = 10;

  constructor(builder: PaginationBuilder)
  {
    this.current = builder.current;
    this.total = builder.total;
    this.size = builder.size;
  }
}

export class PaginationBuilder
{
  private _current: number;
  private _total: number;
  private _size: number;

  setCurrent(value: number)
  {
    this._current = value;
    return this;
  }

  setTotal(value: number)
  {
    this._total = value;
    return this;
  }

  setSize(value: number)
  {
    this._size = value;
    return this;
  }

  get current(): number
  {
    return this._current;
  }

  get total(): number
  {
    return this._total;
  }

  get size(): number
  {
    return this._size;
  }

  build()
  {
    return new Pagination(this);
  }

  static newInstance(): Pagination
  {
    return new PaginationBuilder()
      .setCurrent(1)
      .setTotal(0)
      .setSize(10)
      .build();
  }
}
