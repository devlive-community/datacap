export interface User
{
  username: string;
  thirdConfigure: ThirdConfigure;
  createTime?: string;
  id?: number;
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
  model?: string;
}

export class UserQuestionItem
{
  content: UserAnswer;
  isSelf: boolean;
}

export class UserAnswer
{
  answer: string;
  completionTokens: number;
  promptTokens: number;
  totalTokens: number;
  model: string;
}

export class ThirdConfigure
{
  token = '';
  host = '';
  model = '';
  timeout = 30;
  contentCount = 5
}

export class EditorConfigure
{
  fontSize = 12;
  theme = "chrome";
}
