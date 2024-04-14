import { Type } from '@/views/components/visual/Type'
import { ResponseModel } from '@/model/response'

export class Configuration
{
    title?: null
    headers: never[] = []
    columns: never[] = []
    type: Type = Type.TABLE
    message?: null
    chartConfigure?: IChart
}

export class ConfigurationRequest
{
    public static of(response: ResponseModel): Configuration
    {
        const configuration = new Configuration()
        configuration.headers = response.data.headers
        configuration.columns = response.data.columns
        return configuration
    }
}

export interface IChart
{
    xAxis?: string
    x2Axis?: string
    yAxis?: string
    series?: string
    outerRadius?: number[]
    invalidType?: string
}

export interface ChartField
{
    label?: string
    field?: string
    value?: any
    type?: string
    defaultValues?: any[]
}
