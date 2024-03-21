import { ResponseModel } from '@/model/response'
import { BaseService } from '@/services/base'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH_V1 = '/api/v1/source'

class SourceService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH_V1)
    }

    getSources(page: number, size: number): Promise<ResponseModel>
    {
        return new HttpUtils().get(DEFAULT_PATH_V1, {page: page, size: size})
    }
}

export default new SourceService()
