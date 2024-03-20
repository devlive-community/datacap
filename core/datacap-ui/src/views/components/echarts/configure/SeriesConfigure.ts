import { EchartsType } from '@/views/components/echarts/EchartsType'

export class SeriesConfigure
{
    data: any[] = []
    type: EchartsType = EchartsType.LINE
    smooth = true
    meta = {
        column: null as string | null
    }
}
