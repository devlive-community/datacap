export class Role
{
  id = 0;
  name: string;
  description: string;

  constructor(builder: RoleBuilder)
  {
    this.id = builder.id;
    this.name = builder.name;
    this.description = builder.description;
  }
}

export class RoleBuilder
{
  private _id: number;
  private _name: string;
  private _description: string;

  setId(value: number)
  {
    this._id = value;
    return this;
  }

  setName(value: string)
  {
    this._name = value;
    return this;
  }

  setDescription(value: string)
  {
    this._description = value;
    return this;
  }

  get id(): number
  {
    return this._id;
  }

  get name(): string
  {
    return this._name;
  }

  get description(): string
  {
    return this._description;
  }

  build()
  {
    return new Role(this);
  }
}
