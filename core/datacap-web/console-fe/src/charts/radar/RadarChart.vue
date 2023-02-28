<template>
  <div :style="{width: configure.width, height: configure.height}"
       id="radarContainer">
    <Result v-if="!configure.data" type="warning">
    </Result>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {RadarConfigure} from "@/charts/radar/RadarConfigure";
import * as echarts from "echarts";

export default defineComponent({
  name: "RadarChart",
  props: {
    configure: {
      type: RadarConfigure,
      default: () => new RadarConfigure()
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      setTimeout(() => {
        const indicators = Array<{ name: string, max: number }>();
        const data = [];
        this.configure.data.forEach(ele => {
          indicators.push({name: ele.label, max: 100});
          data.push(ele.percentage);
        });
        const calendarContainer = document.getElementById('radarContainer');
        const calendarChart = echarts.init(calendarContainer);
        calendarChart.setOption({
          tooltip: {},
          radar: {
            shape: 'circle',
            indicator: indicators
          },
          series: [
            {
              name: '',
              type: 'radar',
              data: [{
                value: data
              }]
            }
          ]
        });
      }, 0)
    }
  }
});
</script>
