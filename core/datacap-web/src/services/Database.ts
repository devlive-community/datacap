import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/database';

class DatabaseService
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

  /**
   * Retrieves all items by source ID.
   *
   * @param {number} id - The ID of the source.
   * @return {Promise<ResponseModel>} A promise that resolves to the response model.
   */
  getAllBySource(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().post(`${baseUrl}/source/${id}`);
  }
}

export default new DatabaseService();
