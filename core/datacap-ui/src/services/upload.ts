import { BaseService } from '@/services/base.ts'

const DEFAULT_PATH = '/api/v1/upload'

class UploadService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }
}

export default new UploadService()
