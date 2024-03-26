import { ResponseModel } from '@/model/response'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH = '/api/v1/plugin'

class PluginService
{
    getPlugins(): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${DEFAULT_PATH}`)
    }
}

export default new PluginService()
