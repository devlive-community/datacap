export interface User
{
  username: string;
  thirdConfigure: { chatgpt: ThirdConfigure };
  createTime?: string;
}

export class UserQuestion
{
  content: string;
  type: string;
  newChat = true;
  locale?: string;
  engine?: string;
  error?: string;
  transType?: string;
  from?: string;
}

export class UserQuestionItem
{
  content: string;
  isSelf: boolean;
}

export class ThirdConfigure
{
  token = '';
  proxy = false;
  host = '';
  port = 0;
}
