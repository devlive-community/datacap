export interface TreeData
{
  title: string;
  key: string;
  database: string;
  table: string;
  value: string;
  children: TreeData[];
  dataType: string;
}
