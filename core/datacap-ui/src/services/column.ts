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
     * @param {number} code - The code of the table.
     * @return {Promise<ResponseModel>} A Promise that resolves with the response from the API.
     */
    getAllByTable(code: string): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH}/table/${code}`)
    }
}

export default new ColumnService()