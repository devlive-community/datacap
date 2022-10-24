import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";

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
}
