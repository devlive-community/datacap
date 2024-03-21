import { BaseService } from '@/services/base'

const DEFAULT_PATH = '/api/v1/audit/plugin'

class AuditService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }
}

export default new AuditService()
