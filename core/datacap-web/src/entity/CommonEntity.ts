type EntityProperties = Record<string, unknown>;

type EntityDefaults = {
  id: number;
  name: string;
  description: string;
} & EntityProperties;

class Entity<T extends EntityProperties = EntityProperties>
{
  constructor(public readonly props: T)
  {
  }

  /**
   * Create a new role instance with updated properties.
   */
  public with<K extends keyof T>(props: Pick<T, K>): Entity<T>
  {
    return new Entity({...this.props, ...props} as T);
  }

  /**
   * Create a new role instance with updated `name` property.
   */
  public static default(): Entity<Pick<EntityProperties, 'id' | 'name' | 'description'>>
  {
    return new Entity({
      id: 1,
      name: 'admin',
      description: 'Administrator',
    });
  }

  /**
   * Get the value of the specified property.
   */
  public get<K extends keyof T>(prop: K): T[K]
  {
    return this.props[prop];
  }
}

class EntityBuilder<P extends EntityProperties = EntityProperties>
{
  private readonly defaults: EntityDefaults & P;

  constructor(defaults?: EntityDefaults & P)
  {
    this.defaults = {...(Entity.default().props as EntityDefaults), ...(defaults || Entity.default().props)} as EntityDefaults & P;
  }

  /**
   * Set the specified property.
   */
  public set<K extends keyof EntityProperties>(prop: K, value: EntityProperties[K]): this
  {
    this.defaults[prop] = value as any;
    return this;
  }

  /**
   * Build a role instance using the current defaults.
   */
  public build(): Entity<EntityProperties & P>
  {
    return new Entity(this.defaults as EntityProperties & P);
  }

  /**
   * Create a new role builder instance with `admin` defaults.
   */
  public static default(): EntityBuilder<Pick<EntityProperties, 'name' | 'description'>>
  {
    return new EntityBuilder();
  }
}

const defaultEntity = EntityBuilder.default();

export default defaultEntity;
