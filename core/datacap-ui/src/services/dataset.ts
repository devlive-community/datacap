import { ResponseModel } from '@/model/response'
import { BaseService } from '@/services/base'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH = '/api/v1/dataset'

export class DatasetService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }

    /**
     * Adhoc function for executing custom code.
     *
     * @param {string} code - custom code to execute
     * @param {any} configure - configuration object
     * @return {Promise<ResponseModel>} response from the execution
     */
    adhoc(code: string, configure: any): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH}/adhoc/${code}`, configure)
    }

    /**
     * Retrieves the columns for a given code.
     *
     * @param {string} code - The code to retrieve columns for.
     * @return {Promise<ResponseModel>} A promise that resolves to the response model containing the columns.
     */
    getColumnsByCode(code: string): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${DEFAULT_PATH}/columns/${code}`)
    }
}

export default new DatasetService()
