import {HttpCommon} from "@/common/HttpCommon";
import {ResponseModel} from "@/model/ResponseModel";
import {UserPassword} from "@/model/UserPassword";
import {UserName} from "@/model/UserName";
import {Filter} from "@/model/Filter";
import {EditorConfigure} from "@/model/User";

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

  changeUsername(configure: UserName): Promise<ResponseModel>
  {
    return new HttpCommon().put(baseUrl + '/changeUsername', configure);
  }

  changeThirdConfigure(configure: any): Promise<ResponseModel>
  {
    return new HttpCommon().put(baseUrl + '/changeThirdConfigure', configure);
  }

  getLogs(filter: Filter): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrl + '/log', filter);
  }

  getSugs(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl + '/sugs/' + id);
  }

  getMenus(): Promise<ResponseModel>
  {
    return new HttpCommon().get(baseUrl + '/menus');
  }

  changeEditorConfigure(configure: EditorConfigure): Promise<ResponseModel>
  {
    return new HttpCommon().put(`${baseUrl}/changeEditorConfigure`, configure);
  }
}

export default new UserService();
