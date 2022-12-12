import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";
import {Filter} from "@/model/Filter";
import {Function} from "@/model/settings/function/Function";
import {FunctionImport} from "@/model/settings/function/FunctionImport";

const baseUrl = "/api/v1/admin/function";

class FunctionService
{
  getAllByFilter(filter: Filter): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrl + '/list', filter);
  }

  saveAndUpdate(configure: Function, isUpdate: boolean): Promise<ResponseModel>
  {
    if (isUpdate) {
      return new HttpCommon().put(baseUrl, configure);
    }
    else {
      return new HttpCommon().post(baseUrl, configure);
    }
  }

  getById(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl + "/" + id);
  }

  import(configure: FunctionImport): Promise<ResponseModel>
  {
    return new HttpCommon().put(baseUrl + "/import", configure);
  }

  getByPlugin(plugin: string): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl + "/list/" + plugin);
  }
}

export default new FunctionService();
