import { BaseService } from '@/services/base'

const DEFAULT_PATH = '/api/v1/template'

class TemplateService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }
}

export default new TemplateService()