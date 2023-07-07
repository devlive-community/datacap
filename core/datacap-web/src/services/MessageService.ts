import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";

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
}

export default new MessageService();
