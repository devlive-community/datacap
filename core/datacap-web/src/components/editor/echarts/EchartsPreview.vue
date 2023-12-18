<template>
  <div :style="{width: width, height: height, padding: '0'}"
       id="echartsLineContainer">
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import * as echarts from 'echarts';
import {ChartConfigure} from "@/components/editor/echarts/configure/ChartConfigure";

export default defineComponent({
  name: 'EchartsPreview',
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
        const echartsContainer = document.getElementById('echartsLineContainer');
        const echartsChart = echarts.init(echartsContainer);
        echartsChart.setOption(this.configure);
      }, 0)
    }
  }
});
</script>
