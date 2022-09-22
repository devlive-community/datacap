import { HttpCommon } from "@/common/HttpCommon";
import { ResponseModel } from "@/model/ResponseModel";
import { SourceModel } from "@/model/SourceModel";

const baseUrl = "/api/v1/source";

export class SourceService {
  getSources(page: number, size: number): Promise<ResponseModel> {
    return new HttpCommon().get(baseUrl, { page: page, size: size });
  }

  saveAndUpdate(configure: SourceModel, isUpdate: boolean): Promise<ResponseModel> {
    if (isUpdate) {
      return new HttpCommon().put(baseUrl, JSON.stringify(configure));
    } else {
      return new HttpCommon().post(baseUrl, JSON.stringify(configure));
    }
  }

  delete(id: number): Promise<ResponseModel> {
    return new HttpCommon().delete(baseUrl + '/' + id);
  }

  testConnection(configure: SourceModel): Promise<ResponseModel> {
    return new HttpCommon().post(baseUrl + '/test', JSON.stringify(configure));
  }

  getById(id: number): Promise<ResponseModel> {
    return new HttpCommon().get(baseUrl + "/" + id);
  }
}
