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

export class UserQuestionItem
{
  content: string;
  isSelf: boolean;
}

export class ThirdConfigure
{
  token = '';
}
