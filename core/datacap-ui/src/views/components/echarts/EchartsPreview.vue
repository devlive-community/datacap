<template>
  <div>
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else :style="{width: width, height: height, padding: '0'}" :id="key"/>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import * as echarts from 'echarts'
import { v4 as uuidv4 } from 'uuid'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { ChartConfigure } from './configure/ChartConfigure'
import ReportService from '@/services/report'
import { getValueByKey } from './DataUtils'
import { SeriesConfigure } from './configure/SeriesConfigure'
import { ExecuteModel } from '@/model/execute'
import ExecuteService from '@/services/execute'

export default defineComponent({
  name: 'EchartsPreview',
  components: {CircularLoading},
  props: {
    width: {
      type: String,
      default: () => '100%'
    },
    height: {
      type: String,
      default: () => '300px'
    },
    configure: {
      type: Object as () => ChartConfigure | null
    },
    id: {
      type: Number
    }
  },
  watch: {
    width: 'handlerInitialize',
    height: 'handlerInitialize'
  },
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      key: ''
    }
  },
  methods: {
    handlerInitialize()
    {
      this.key = uuidv4()
      setTimeout(() => {
        try {
          if (this.key) {
            const echartsContainer = document.getElementById(this.key)
            const echartsChart = echarts.init(echartsContainer)
            echartsChart.resize()
            if (this.id) {
              this.loading = true
              ReportService.getById(this.id)
                  .then(response => {
                    if (response.status && response.data.realtime) {
                      if (response.data.source) {
                        const queryConfigure: ExecuteModel = {
                          name: response.data.source.id,
                          content: response.data.query,
                          format: 'JSON',
                          mode: 'REPORT'
                        }
                        ExecuteService.execute(queryConfigure, null)
                            .then(response => {
                              const configure: any = this.configure as ChartConfigure;
                              configure.xAxis.data = getValueByKey(configure.xAxis.meta.column, response.data.columns)
                              configure.yAxis.data = getValueByKey(configure.yAxis.meta.column, response.data.columns)
                              configure.series.forEach((item: SeriesConfigure) => {
                                const series: SeriesConfigure = item as SeriesConfigure
                                series.data = getValueByKey(series.meta.column as string, response.data.columns)
                              })
                              echartsChart.setOption(configure)
                            })
                            .finally(() => this.loading = false)
                      }
                    }
                    else {
                      echartsChart.setOption(this.configure as any)
                    }
                  })
                  .finally(() => this.loading = false)
            }
            else {
              echartsChart.setOption(this.configure as any)
            }
          }
        }
        catch (e) {
          console.error(e)
        }
      }, 0)
    }
  }
});
</script>
