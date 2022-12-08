import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";
import {Filter} from "@/model/Filter";
import {TemplateSql} from "@/model/template/Sql";
import {SqlBody} from "@/model/template/SqlBody";

const baseUrl = "/api/v1/admin/template/sql";

class TemplateSqlService
{
  getAllTemplateSql(filter: Filter): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrl + '/list', filter);
  }

  saveAndUpdate(configure: TemplateSql, isUpdate: boolean): Promise<ResponseModel>
  {
    configure.configure = null;
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

  execute(configure: SqlBody): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrl + "/execute", configure);
  }
}

export default new TemplateSqlService();
