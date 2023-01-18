import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";
import {SourceModel} from "@/model/SourceModel";
import {SharedSource} from "@/model/SharedSource";
import {isEmpty} from "lodash";

const baseUrl = "/api/v1/source";
const baseUrlV2 = "/api/v2/source";

export class SourceService
{
  getSources(page: number, size: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl, {page: page, size: size});
  }

  saveAndUpdate(configure: SourceModel, isUpdate: boolean): Promise<ResponseModel>
  {
    configure.protocol = isEmpty(configure.protocol) ? 'HTTP' : configure.protocol;
    if (isUpdate) {
      return new HttpCommon().put(baseUrl, JSON.stringify(configure));
    }
    else {
      return new HttpCommon().post(baseUrl, JSON.stringify(configure));
    }
  }

  delete(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().delete(baseUrl + '/' + id);
  }

  testConnection(configure): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrlV2 + '/test', configure);
  }

  getById(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl + "/" + id);
  }

  getPlugins(): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl + "/plugins");
  }

  shared(configure: SharedSource): Promise<ResponseModel>
  {
    return new HttpCommon().put(baseUrl + "/shared", JSON.stringify(configure));
  }
}
