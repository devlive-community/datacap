export interface TemplateSql
{
  id?: number;
  name: string;
  description: string;
  content: string;
  plugin: string;
  configure: NonNullable<unknown>;
  system: boolean;
  createTime?: string;
  updateTime?: string;
}
