import { UserRequest } from '@/model/user/request/user'
import { ResponseModel } from '@/model/response'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH_AUTH = '/api/auth';

class UserService
{
    /**
     * Sign in with the given user request configuration.
     *
     * @param {UserRequest} configure - the user request configuration
     * @return {Promise<ResponseModel>} a promise containing the response model
     */
    signin(configure: UserRequest): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH_AUTH}/signin`, configure)
    }

    /**
     * Signup user with the given configuration.
     *
     * @param {UserRequest} configure - the user configuration
     * @return {Promise<ResponseModel>} the response model
     */
    signup(configure: UserRequest): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH_AUTH}/signup`, configure);
    }
}

export default new UserService()