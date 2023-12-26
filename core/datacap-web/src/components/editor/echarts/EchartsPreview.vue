<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <div :style="{width: width, height: height, padding: '0'}"
         :id="key">
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import * as echarts from 'echarts';
import {v4 as uuidv4} from 'uuid';
import {ChartConfigure} from "@/components/editor/echarts/configure/ChartConfigure";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import ReportService from "@/services/admin/ReportService";
import {getValueByKey} from "@/components/editor/echarts/DataUtils";
import {SeriesConfigure} from "@/components/editor/echarts/configure/SeriesConfigure";
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";

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
      type: ChartConfigure,
      default: () => null
    },
    id: {
      type: Number
    }
  },
  watch: {
    width: 'handlerInitialize',
    height: 'handlerInitialize',
  },
  created()
  {
    this.handlerInitialize();
  },
  data()
  {
    return {
      loading: false,
      key: null
    }
  },
  methods: {
    handlerInitialize()
    {
      this.key = uuidv4();
      setTimeout(() => {
        try {
          const echartsContainer = document.getElementById(this.key);
          const echartsChart = echarts.init(echartsContainer);
          echartsChart.resize()
          if (this.id) {
            this.loading = true
            ReportService.getById(this.id)
              .then(response => {
                if (response.status && response.data.realtime) {
                  const queryConfigure: ExecuteModel = {
                    name: response.data.source.id,
                    content: response.data.query,
                    format: "JSON"
                  }
                  new ExecuteService()
                    .execute(queryConfigure, null)
                    .then(response => {
                      const configure: any = this.configure as ChartConfigure;
                      configure.xAxis.data = getValueByKey(configure.xAxis.meta.column, response.data.columns);
                      configure.yAxis.data = getValueByKey(configure.yAxis.meta.column, response.data.columns);
                      configure.series.forEach(item => {
                        const series: SeriesConfigure = item as SeriesConfigure;
                        series.data = getValueByKey(series.meta.column, response.data.columns);
                      })
                      echartsChart.setOption(configure);
                    })
                    .finally(() => this.loading = false)
                }
                else {
                  echartsChart.setOption(this.configure);
                }
              })
              .finally(() => this.loading = false)
          }
          else {
            echartsChart.setOption(this.configure);
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
