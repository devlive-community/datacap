import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

export class SourceService
{
  getSources(): Promise<ResponseModel>
  {
    return new HttpCommon()
      .get("/api/v1/source");
  }
}
