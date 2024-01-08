import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/dataset';

class DatasetService
  extends BaseService<any>
{
  constructor()
  {
    super(baseUrl);
  }

  saveOrUpdate<T>(configure: T): Promise<ResponseModel>
  {
    const url = `${baseUrl}/create`
    if (configure['id'] > 0) {
      return new HttpCommon().put(url, configure);
    }
    else {
      return new HttpCommon().post(url, configure);
    }
  }

  rebuild(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().put(`${baseUrl}/rebuild/${id}`);
  }

  getColumns(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}/getColumns/${id}`);
  }

  getColumnsByCode(code: string): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}/columns/${code}`);
  }

  syncData(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().put(`${baseUrl}/syncData/${id}`);
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }
}

export default new DatasetService();
