import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";
import {UserPassword} from "@/model/UserPassword";

const baseUrl = "/api/v1/user";

class UserService
{
  getInfo(): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl);
  }

  changePassword(configure: UserPassword): Promise<ResponseModel>
  {
    return new HttpCommon().put(baseUrl + '/changePassword', configure);
  }
}

export default new UserService();
