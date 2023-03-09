export interface User
{
  username: string;
  thirdConfigure: { chatgpt: ThirdConfigure };
  createTime?: string;
}

export class UserQuestion
{
  question: string;
  type: string;
}

export class ThirdConfigure
{
  token = '';
}
