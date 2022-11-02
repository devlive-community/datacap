import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";

const baseUrl = "/api/v1/user";

class UserService
{
  getInfo(): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl);
  }
}

export default new UserService();
