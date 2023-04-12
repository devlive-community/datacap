import {AxisConfigure} from "@/components/editor/echarts/configure/AxisConfigure";
import {SeriesConfigure} from "@/components/editor/echarts/configure/SeriesConfigure";
import {TooltipConfigure} from "@/components/editor/echarts/configure/TooltipConfigure";

export class ChartConfigure
{
  xAxis: AxisConfigure = null;
  yAxis: AxisConfigure = null;
  series: Array<SeriesConfigure> = null;
  tooltip: TooltipConfigure = new TooltipConfigure();
}

export default function toOptions(options: ChartConfigure): any {
  return JSON.stringify(options);
}
