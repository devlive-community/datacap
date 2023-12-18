import {ResponseModel} from '@/model/ResponseModel';
import {BaseService} from '@/services/BaseService';

const baseUrl = '/api/v1/report';

class ReportService
  extends BaseService<any>
{
  constructor()
  {
    super(baseUrl);
  }

  deleteById(id: number): Promise<ResponseModel>
  {
    throw new Error('Method not implemented.');
  }

  getByName<T>(name: string): Promise<ResponseModel>
  {
    return Promise.resolve(undefined);
  }
}

export default new ReportService();
