import {Filter} from "@/model/Filter";
import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";

export abstract class BaseService<T>
{
  private readonly baseUrl: string;

  protected constructor(baseUrl: string)
  {
    this.baseUrl = baseUrl;
  }

  /**
   * Filter the corresponding data according to the filter
   * <p>
   *   If the filter is empty, all data will be returned.
   *   Supported filters:
   *    <ul>
   *      <li>paging</li>
   *      <li>order</li>
   *    </ul>
   * </p>
   * @param {Filter} filter
   * @param filter
   */
  getAll<T>(filter: Filter): Promise<ResponseModel>
  {
    return new HttpCommon().post(`${this.baseUrl}/list`, filter);
  }

  /**
   * Filter the corresponding data according to the id
   * @param id
   */
  getById<T>(id: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${this.baseUrl}/${id}`);
  }

  /**
   * Report an error or update data, if id > 0 in the data, it is updated, otherwise it is saved
   * @param configure
   */
  saveOrUpdate<T>(configure: T): Promise<ResponseModel>
  {
    if (configure['id'] > 0) {
      return new HttpCommon().put(this.baseUrl, configure);
    }
    else {
      return new HttpCommon().post(this.baseUrl, configure);
    }
  }

  /**
   * Filter the corresponding data according to the name
   * @param name
   */
  abstract getByName<T>(name: string): Promise<ResponseModel>;

  abstract deleteById(id: number): Promise<ResponseModel>;
}
