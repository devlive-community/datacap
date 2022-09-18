export interface PageModel<T>
{
  page: number;
  size: number;
  total: number;
  totalPage: number;
  content: T[]
}
