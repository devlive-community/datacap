import { BaseModel } from '@/model/base'
import { DatasetModel } from '@/model/dataset'

export interface ReportModel
    extends BaseModel
{
    configure?: string
    realtime?: boolean
    type?: string
    query?: string
    dataset?: DatasetModel
}