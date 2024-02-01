import {Type} from "@/components/visual/Type";

export class Configuration
{
  headers: [] = [];
  columns: [] = [];
  type: Type = Type.TABLE;
  message: null;
  chartConfigure? = {
    xAxis: null,
    yAxis: null,
    outerRadius: null
  }
}
