import { UserRequest } from '@/model/user/request/user'
import { ResponseModel } from '@/model/response'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH_AUTH = '/api/auth';

class UserService
{
    signin(configure: UserRequest): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH_AUTH}/signin`, configure)
    }
}

export default new UserService()