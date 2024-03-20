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
    xAxis: null
    x2Axis: null
    yAxis: null
    series: null
    outerRadius: number[]
    invalidType: null
}
