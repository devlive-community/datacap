import { BaseService } from '@/services/base'
import { ResponseModel } from '@/model/response'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH = '/api/v1/function'

class FunctionService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }

    // import(configure: FunctionsImport): Promise<ResponseModel>
    // {
    //     return new HttpCommon().put(baseUrl + '/import', configure);
    // }

    getByPlugin(plugin: string): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${DEFAULT_PATH}/list/${plugin}`)
    }
}

export default new FunctionService()