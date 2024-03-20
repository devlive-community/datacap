export interface UserRequest
{
    username: string | null
    password: string | null
    confirmPassword?: string
    captcha?: string
    timestamp?: number
    // Marks the error message returned after an operation
    message?: string
    loading?: boolean
}
