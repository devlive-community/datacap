import { UserRequest } from '@/model/user/request/user'
import { ResponseModel } from '@/model/response'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH_AUTH = '/api/auth'
const DEFAULT_PATH_USER = '/api/v1/user'

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
        return new HttpUtils().post(`${DEFAULT_PATH_AUTH}/signup`, configure)
    }

    /**
     * Get menus from the server.
     *
     * @return {Promise<ResponseModel>} The response model containing the menus.
     */
    getMenus(): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${DEFAULT_PATH_USER}/menus`)
    }

    /**
     * Get information through an asynchronous operation.
     *
     * @return {Promise<ResponseModel>} the response model from the operation
     */
    getInfo(): Promise<ResponseModel>
    {
        return new HttpUtils().get(DEFAULT_PATH_USER)
    }
}

export default new UserService()