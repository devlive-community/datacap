import {ResponseModel} from '@/model/ResponseModel'
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/plugin'

class PluginService
{
  getPlugins(): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}`);
  }
}

export default new PluginService()

