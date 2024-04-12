import { ResponseModel } from '@/model/response'
import { BaseService } from '@/services/base'
import { HttpUtils } from '@/utils/http'
import { SourceModel } from '@/model/source.ts'
import { isEmpty } from 'lodash'
import { FilterModel } from '@/model/filter.ts'

const DEFAULT_PATH_V1 = '/api/v1/source'
const DEFAULT_PATH_V2 = '/api/v2/source'

class SourceService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH_V2)
    }

    getSources(page: number, size: number): Promise<ResponseModel>
    {
        return new HttpUtils().get(DEFAULT_PATH_V1, { page: page, size: size })
    }

    getByCode(code: string): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${ DEFAULT_PATH_V2 }/code/${ code }`)
    }

    getPlugins(): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${ DEFAULT_PATH_V1 }/plugins`)
    }

    testConnection(configure: any)
    {
        return new HttpUtils().post(`${ DEFAULT_PATH_V2 }/test`, configure)
    }

    getById(id: number): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${ DEFAULT_PATH_V2 }/${ id }`)
    }

    saveOrUpdate(configure: SourceModel): Promise<ResponseModel>
    {
        configure.protocol = isEmpty(configure.protocol) ? 'HTTP' : configure.protocol
        if (configure.id && configure['id'] > 0) {
            return new HttpUtils().put(DEFAULT_PATH_V2, JSON.stringify(configure))
        }
        else {
            return new HttpUtils().post(DEFAULT_PATH_V2, JSON.stringify(configure))
        }
    }

    syncMetadata(id: number): Promise<ResponseModel>
    {
        return new HttpUtils().put(`${ DEFAULT_PATH_V2 }/syncMetadata/${ id }`)
    }

    getHistory(id: number, configure: FilterModel): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${ DEFAULT_PATH_V2 }/getHistory/${ id }`, configure)
    }
}

export default new SourceService()
