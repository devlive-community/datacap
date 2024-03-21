import { ResponseModel } from '@/model/response';
import { BaseService } from '@/services/base'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH = '/api/v1/column'

class ColumnService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }

    /**
     * Fetches all items from a table based on the provided ID.
     *
     * @param {number} id - The ID of the table.
     * @return {Promise<ResponseModel>} A Promise that resolves with the response from the API.
     */
    getAllByTable(id: number): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH}/table/${id}`)
    }
}

export default new ColumnService()