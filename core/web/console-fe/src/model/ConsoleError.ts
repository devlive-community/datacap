export interface ConsoleError
{
  message: string;
  info: string;
  url: string;
  stack: any;
  read: false;
  vm: any;
}
