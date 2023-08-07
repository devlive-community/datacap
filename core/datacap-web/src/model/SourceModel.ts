export interface SourceModel
{
  id?: number;
  name: string;
  description: string;
  protocol: string;
  host: string;
  port: number;
  username: string;
  password: string;
  catalog: string;
  database: string;
  type: string;
  createTime?: number;
  ssl?: boolean;
  configures: {};
  file?: [];
  version?: string;
}
