export interface AuthUser
{
  username: string;
  password: string;
  captcha?: string;
  timestamp?: number;
  // Marks the error message returned after an operation
  message?: string;
  loading?: boolean;
}
