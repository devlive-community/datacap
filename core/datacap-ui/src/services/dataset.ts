import { ResponseModel } from '@/model/response'
import { BaseService } from '@/services/base'
import { HttpUtils } from '@/utils/http'
import { FilterModel } from '@/model/filter'

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

    /**
     * Rebuilds the specified item identified by the given ID.
     *
     * @param {number} id - the ID of the item to be rebuilt
     * @return {Promise<ResponseModel>} a Promise that resolves with the response from the rebuild request
     */
    rebuild(id: number): Promise<ResponseModel>
    {
        return new HttpUtils().put(`${DEFAULT_PATH}/rebuild/${id}`)
    }

    /**
     * Retrieves the history for a given code using the provided pagination model.
     *
     * @param {string} code - The code for which to retrieve the history.
     * @param {FilterModel} configure - The file model to use for the request.
     * @return {Promise<ResponseModel>} A promise that resolves with the response model containing the history data.
     */
    getHistory(code: string, configure: FilterModel): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH}/history/${code}`, configure)
    }

    /**
     * Sync data with the server using the provided id.
     *
     * @param {number} id - The id of the data to sync
     * @return {Promise<ResponseModel>} A promise that resolves with the response from the server
     */
    syncData(id: number): Promise<ResponseModel>
    {
        return new HttpUtils().put(`${DEFAULT_PATH}/syncData/${id}`)
    }

    /**
     * Clears the data associated with the given code.
     *
     * @param {string} code - the code for which data needs to be cleared
     * @return {Promise<ResponseModel>} a Promise that resolves with the response from the server
     */
    clearData(code: string): Promise<ResponseModel>
    {
        return new HttpUtils().put(`${DEFAULT_PATH}/clearData/${code}`)
    }
}

export default new DatasetService()
