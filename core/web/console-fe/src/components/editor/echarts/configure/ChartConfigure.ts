import {AxisConfigure} from "@/components/editor/echarts/configure/AxisConfigure";
import {SeriesConfigure} from "@/components/editor/echarts/configure/SeriesConfigure";
import {TooltipConfigure} from "@/components/editor/echarts/configure/TooltipConfigure";
import {EchartsType} from "@/components/editor/echarts/EchartsType";

export class ChartConfigure
{
  type = EchartsType.LINE;
  xAxis: AxisConfigure = new AxisConfigure();
  yAxis: AxisConfigure = new AxisConfigure();
  series: Array<SeriesConfigure> = new Array();
  tooltip: TooltipConfigure = new TooltipConfigure();
}

export default function toOptions(options: ChartConfigure): any {
  return JSON.stringify(options);
}
