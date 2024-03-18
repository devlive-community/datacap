import { SeriesConfigure } from '@/views/components/echarts/configure/SeriesConfigure'
import { AxisConfigure } from '@/views/components/echarts/configure/AxisConfigure'
import { TooltipConfigure } from '@/views/components/echarts/configure/TooltipConfigure'

export class ChartConfigure
{
    xAxis: AxisConfigure | undefined
    yAxis: AxisConfigure | undefined
    series: Array<SeriesConfigure> | undefined
    tooltip: TooltipConfigure = new TooltipConfigure()
}

export default function toOptions(options: ChartConfigure): any {
    return JSON.stringify(options)
}
