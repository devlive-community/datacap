import {ResponseModel} from '@/model/ResponseModel'
import {BaseService} from "@/services/BaseService";
import {Role} from "@/services/admin/RoleModel";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/role'

class RoleService
  extends BaseService<Role>
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

  getAllMenuById(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}/${id}/menus`)
  }

  saveMenu(id: number, menus: []): Promise<ResponseModel>
  {
    return new HttpCommon().put(`${baseUrl}/${id}/menus`, menus)
  }
}

export default new RoleService()
