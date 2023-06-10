import {ResponseModel} from '@/model/ResponseModel'
import {BaseService} from "@/services/BaseService";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/user'

class UserService
  extends BaseService<any>
{

  constructor()
  {
    super(baseUrl);
  }

  deleteById(id: number): Promise<ResponseModel>
  {
    throw new Error('Method not implemented.');
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }

  allocationRole(configure: any): Promise<ResponseModel>
  {
    return new HttpCommon().put(`${baseUrl}/allocationRole`, configure)
  }
}

export default new UserService()
