import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";
import {SourceModel} from "@/model/SourceModel";

export class SourceService
{
  getSources(): Promise<ResponseModel>
  {
    return new HttpCommon()
      .get("/api/v1/source");
  }

  saveAndUpdate(configure: SourceModel): Promise<ResponseModel>
  {
    return new HttpCommon()
      .post("/api/v1/source", JSON.stringify(configure));
  }
}
