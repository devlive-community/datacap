import { ResponseModel } from '@/model/response';
import { BaseService } from '@/services/base'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH = '/api/v1/snippet'

class SnippetService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }

    getSnippets(page: number, size: number): Promise<ResponseModel>
    {
        return new HttpUtils().get(DEFAULT_PATH, {page: page, size: size});
    }
}

export default new SnippetService()
