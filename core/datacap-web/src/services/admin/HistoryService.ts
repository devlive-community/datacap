import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/audit/plugin';

class HistoryService
  extends BaseService<any>
{
  constructor()
  {
    super(baseUrl);
  }

  getData(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}/data/${id}`);
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }
}

export default new HistoryService();
