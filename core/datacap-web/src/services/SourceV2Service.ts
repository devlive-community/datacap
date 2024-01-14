import {ResponseModel} from "@/model/ResponseModel";
import {isEmpty} from "lodash";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = "/api/v2/source";

class SourceV2Service
{
  saveAndUpdate(configure, isUpdate: boolean): Promise<ResponseModel>
  {
    configure.protocol = isEmpty(configure.protocol) ? 'HTTP' : configure.protocol;
    if (isUpdate) {
      return new HttpCommon().put(baseUrl, JSON.stringify(configure));
    }
    else {
      return new HttpCommon().post(baseUrl, JSON.stringify(configure));
    }
  }

  getById(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl + "/" + id);
  }

  getHistory(id: number, configure: any): Promise<ResponseModel>
  {
    return new HttpCommon().post(`${baseUrl}/getHistory/${id}`, configure);
  }
}

export default new SourceV2Service();
