import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";

const baseUrl = '/api/v1/dataset';

class DatasetService
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

export default new DatasetService();
