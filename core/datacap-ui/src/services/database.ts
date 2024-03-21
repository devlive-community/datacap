import { BaseService } from '@/services/base'
import { ResponseModel } from '@/model/response'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH = '/api/v1/database'

class DatabaseService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }

    /**
     * Retrieves all items by source ID.
     *
     * @param {number} id - The ID of the source.
     * @return {Promise<ResponseModel>} A promise that resolves to the response model.
     */
    getAllBySource(id: string): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${DEFAULT_PATH}/source/${id}`)
    }
}

export default new DatabaseService()