import {ResponseModel} from '@/model/ResponseModel'
import {BaseService} from "@/services/BaseService";
import {HttpCommon} from "@/common/HttpCommon";
import {PipelineModel} from "@/model/Pipeline";

const baseUrl = '/api/v1/pipeline'

class PipelineService
  extends BaseService<any>
{

  constructor()
  {
    super(baseUrl);
  }

  submit(configure: PipelineModel): Promise<ResponseModel>
  {
    configure.from.source = undefined
    configure.to.source = undefined
    return new HttpCommon().post(`${baseUrl}/submit`, configure);
  }

  logger(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}/log/${id}`);
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }
}

export default new PipelineService()
