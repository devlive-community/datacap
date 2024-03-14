import { BaseService } from '@/services/base'

const DEFAULT_PATH = '/api/v1/schedule'

class ScheduleService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }
}

export default new ScheduleService()
