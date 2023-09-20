import {ResponseModel} from '@/model/ResponseModel'
import {BaseService} from "@/services/BaseService";
import {Role} from "@/services/admin/RoleModel";
import {Filter} from "@/model/Filter";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/admin/schedule'

class ScheduleService
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

  getScheduleHistory(filter: Filter, id: number): Promise<ResponseModel>
  {
    return new HttpCommon().post(`${baseUrl}/${id}/history`, filter);
  }
}

export default new ScheduleService()
