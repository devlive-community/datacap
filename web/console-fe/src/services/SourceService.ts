import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";
import {SourceModel} from "@/model/SourceModel";

const baseUrl = "/api/v1/source";

export class SourceService
{
  getSources(): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl);
  }

  saveAndUpdate(configure: SourceModel): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrl, JSON.stringify(configure));
  }

  delete(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().delete(baseUrl + '/' + id);
  }
}
