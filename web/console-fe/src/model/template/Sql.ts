export interface TemplateSql
{
  id?: number;
  name: string;
  description: string;
  content: string;
  plugin: string;
  configure: {};
  createTime?: string;
  updateTime?: string;
}
