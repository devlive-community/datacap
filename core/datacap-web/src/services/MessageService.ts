import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {UserQuestion} from "@/model/User";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/message';

export class MessageService
  extends BaseService<any>
{
  constructor()
  {
    super(baseUrl);
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }

  aiReply(configure: UserQuestion): Promise<ResponseModel>
  {
    return new HttpCommon().post(baseUrl + '/ai/reply', configure);
  }
}

export default new MessageService();
