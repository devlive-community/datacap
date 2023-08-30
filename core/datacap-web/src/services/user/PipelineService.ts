import {ResponseModel} from '@/model/ResponseModel'
import {BaseService} from "@/services/BaseService";

const baseUrl = '/api/v1/pipeline'

class PipelineService
  extends BaseService<any>
{

  constructor()
  {
    super(baseUrl);
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }
}

export default new PipelineService()
