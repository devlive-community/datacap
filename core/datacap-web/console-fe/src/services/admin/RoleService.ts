import {ResponseModel} from '@/model/ResponseModel'
import {BaseService} from "@/services/BaseService";
import {Role} from "@/services/admin/RoleModel";

const baseUrl = '/api/v1/admin/role'

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
}

export default new RoleService()
