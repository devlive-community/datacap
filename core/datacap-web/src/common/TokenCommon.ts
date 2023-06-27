import {AuthResponse} from "@/model/AuthResponse";
import Common from "@/common/Common";

export class TokenCommon
{
  public static getAuthUser(): AuthResponse
  {
    try {
      return JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
    }
    catch (error) {
      return undefined;
    }
  }
}
