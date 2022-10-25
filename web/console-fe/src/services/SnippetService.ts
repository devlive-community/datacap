import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";
import {Snippet} from "@/model/Snippet";

const baseUrl = "/api/v1/snippet";

export class SnippetService
{
  getSnippets(page: number, size: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl, {page: page, size: size});
  }

  delete(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().delete(baseUrl + '/' + id);
  }

  saveAndUpdate(configure: Snippet, isUpdate: boolean): Promise<ResponseModel>
  {
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
}
