import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";
import {AuthUser} from "@/model/AuthUser";

const defaultAuth = "/api/auth";

export class AuthService
{
  signin(configure: AuthUser): Promise<ResponseModel>
  {
    return new HttpCommon().post(defaultAuth + '/signin', configure);
  }

  signup(configure: AuthUser): Promise<ResponseModel>
  {
    return new HttpCommon().post(defaultAuth + '/signup', configure);
  }
}
