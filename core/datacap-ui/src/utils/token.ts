import { AuthResponse } from '@/model/user/response/auth'
import Common from '@/utils/common'

export class TokenUtils
{
    public static getAuthUser(): AuthResponse | undefined
    {
        try {
            return JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse
        }
        catch (error) {
            return undefined
        }
    }
}
