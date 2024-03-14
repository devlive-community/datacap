import { BaseService } from '@/services/base'

const DEFAULT_PATH = '/api/v1/role'

class RoleService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }
}

export default new RoleService()
