import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";
import {ExecuteModel} from "@/model/ExecuteModel";

const baseUrl = "/api/v1/execute";

export class ExecuteService
{
  execute(configure: ExecuteModel, cancelToken: any): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrl, JSON.stringify(configure), cancelToken);
  }
}
