import {SqlBody} from "@/model/builder/SqlBody";

export class ExecuteDslBody
{
  name: string;
  configure: SqlBody;
  env?: object;
  format?: string;

  constructor(builder: ExecuteDslBodyBuilder)
  {
    this.name = builder.name;
    this.configure = builder.configure;
    this.format = builder.format;
  }
}

export class ExecuteDslBodyBuilder
{
  private readonly _name: string;
  private readonly _format: string;
  private _configure: SqlBody;

  constructor(name: string, format: string)
  {
    this._name = name;
    this._format = format;
  }

  setConfigure(configure: SqlBody)
  {
    this._configure = configure;
    return this;
  }

  get name()
  {
    return this._name;
  }

  get configure()
  {
    return this._configure;
  }

  get format()
  {
    return this._format;
  }

  build()
  {
    return new ExecuteDslBody(this);
  }
}
