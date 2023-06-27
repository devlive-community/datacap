<template>
  <div :style="{width: configure.width, height: configure.height}"
       id="calendarContainer">
    <Result v-if="!configure.data" type="warning">
    </Result>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {CalendarConfigure} from '@/charts/calendar/CalendarConfigure';
import * as echarts from 'echarts';
import {max} from "lodash";

export default defineComponent({
  name: 'CalendarChart',
  props: {
    configure: {
      type: CalendarConfigure,
      default: () => new CalendarConfigure()
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
        if (this.configure?.data) {
          const calendarContainer = document.getElementById('calendarContainer');
          const calendarChart = echarts.init(calendarContainer);
          calendarChart.setOption({
            tooltip: {
              formatter: function (params) {
                return params.data[0] + ' : ' + params.data[1];
              }
            },
            visualMap: {
              min: 0,
              max: max(this.configure.data.map(ele => ele.count)),
              type: 'piecewise',
              orient: 'horizontal',
              left: 'center',
              top: 0
            },
            calendar: {
              top: 50,
              left: 30,
              right: 10,
              cellSize: [20, 20],
              range: new Date().getFullYear(),
              itemStyle: {
                borderWidth: 0.5
              }
            },
            series: {
              type: 'heatmap',
              coordinateSystem: 'calendar',
              data: this.configure.data
                .map(ele => {
                  return [ele.date, ele.count]
                })
            }
          });
        }
      }, 0)
    }
  }
});
</script>
