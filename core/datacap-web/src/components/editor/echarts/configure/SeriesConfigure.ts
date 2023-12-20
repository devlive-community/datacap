import {EchartsType} from "@/components/editor/echarts/EchartsType";

export class SeriesConfigure
{
  data: any[] = [];
  type: EchartsType = EchartsType.LINE;
  smooth = true;
  meta = {column: null}
}
