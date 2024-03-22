import { Type } from '@/views/components/visual/Type'

export class Configuration
{
    title?: null
    headers: never[] = []
    columns: never[] = []
    type: Type = Type.TABLE
    message?: null
    chartConfigure?: IChart
}

export interface IChart
{
    xAxis?: null | string
    x2Axis?: null | string
    yAxis?: null | string
    series?: null | string
    outerRadius?: number[]
    invalidType?: null | string
}
