import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/table';

class TableService
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
   * Retrieves all data from the database by the specified ID.
   *
   * @param {number} id - The ID of the database.
   * @return {Promise<ResponseModel>} A promise that resolves to a ResponseModel object.
   */
  getAllByDatabase(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().post(`${baseUrl}/database/${id}`);
  }
}

export default new TableService();
