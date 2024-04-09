import { BaseService } from '@/services/base'
import { ResponseModel } from '@/model/response.ts'
import { HttpUtils } from '@/utils/http.ts'

const DEFAULT_PATH = '/api/v1/pipeline'

class PipelineService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }

    getLogger(id: number): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${ DEFAULT_PATH }/log/${ id }`)
    }
}

export default new PipelineService()