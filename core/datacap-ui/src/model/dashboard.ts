import { UserModel } from '@/model/user.ts'
import { ReportModel } from '@/model/report.ts'

export interface DashboardModel
{
    id?: number
    name?: string
    configure?: string
    version?: string
    description?: string
    createTime?: string
    updateTime?: string
    user?: UserModel
    reports?: ReportModel[]
}

export class DashboardRequest
{
    public static of(): DashboardModel
    {
        return {
            id: undefined,
            name: undefined,
            configure: undefined,
            version: undefined,
            description: undefined,
            reports: new Array<ReportModel>()
        }
    }
}
