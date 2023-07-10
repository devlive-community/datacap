import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/chat';

export class ChatService
  extends BaseService<any>
{
  constructor()
  {
    super(baseUrl);
  }

  getMessages(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}/${id}/messages`);
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }
}

export default new ChatService();
