import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/column';

class ColumnService
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
   * Fetches all items from a table based on the provided ID.
   *
   * @param {number} id - The ID of the table.
   * @return {Promise<ResponseModel>} A Promise that resolves with the response from the API.
   */
  getAllByTable(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().post(`${baseUrl}/table/${id}`);
  }
}

export default new ColumnService();
