import {BaseService} from "./BaseService";
import {ResponseModel} from "../model/ResponseModel";
import {HttpCommon} from "../common/HttpCommon";

const baseUrl = '/api/v1/dashboard';

class DashboardService
  extends BaseService<any>
{
  constructor()
  {
    super(baseUrl);
  }

  deleteById(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().delete(`${baseUrl}/${id}`);
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }
}

export default new DashboardService();
