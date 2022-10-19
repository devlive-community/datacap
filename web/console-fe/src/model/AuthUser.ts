export interface AuthUser
{
  username: string;
  password: string;
  // Marks the error message returned after an operation
  message?: string;
  loading?: boolean;
}
