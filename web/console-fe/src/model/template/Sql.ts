export interface TemplateSql
{
  id?: number;
  name: string;
  description: string;
  content: string;
  plugin: string;
  configure: {};
  system: boolean;
  createTime?: string;
  updateTime?: string;
}
