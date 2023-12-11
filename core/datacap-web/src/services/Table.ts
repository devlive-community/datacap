import {BaseService} from "@/services/BaseService";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";
import {TableFilter} from "@/model/TableFilter";
import {ExportBody} from "@/model/ExportBody";

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

  /**
   * Retrieves data for a specific ID using the provided table filter configuration.
   *
   * @param {number} id - The ID of the data to retrieve.
   * @param {TableFilter} configure - The table filter configuration.
   * @return {Promise<ResponseModel>} - A promise that resolves to the response model.
   */
  getData(id: number, configure: TableFilter): Promise<ResponseModel>
  {
    if (!configure) {
      configure = <TableFilter>{};
    }
    return new HttpCommon().post(`${baseUrl}/${id}`, configure);
  }

  /**
   * A description of the entire function.
   *
   * @param {number} id - The identifier of the data.
   * @param {any} configure - The configuration object.
   * @return {Promise<ResponseModel>} A promise that resolves to the response model.
   */
  putData(id: number, configure: TableFilter): Promise<ResponseModel>
  {
    return new HttpCommon().put(`${baseUrl}/${id}`, configure);
  }

  /**
   * Export data with the given id and configuration.
   *
   * @param {number} id - The id of the data to export.
   * @param {ExportBody} configure - The configuration for the export.
   * @return {Promise<ResponseModel>} - A promise that resolves to a ResponseModel.
   */
  exportData(id: number, configure: ExportBody): Promise<ResponseModel>
  {
    return new HttpCommon().post(`${baseUrl}/export/${id}`, configure);
  }
}

export default new TableService();
