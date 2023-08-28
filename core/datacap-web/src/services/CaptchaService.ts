import {ResponseModel} from '@/model/ResponseModel';
import {BaseService} from '@/services/BaseService';
import {HttpCommon} from "@/common/HttpCommon";

const baseUrl = '/api/v1/captcha';

class CaptchaService
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
   * Retrieves a captcha for the given timestamp.
   *
   * @param {number} timestamp - The timestamp for which to retrieve the captcha.
   * @return {Promise<ResponseModel>} A Promise that resolves to the captcha response.
   */
  getCaptcha(timestamp: number): Promise<ResponseModel>
  {
    return new HttpCommon().get(`${baseUrl}?timestamp=${timestamp}`);
  }
}

export default new CaptchaService();
