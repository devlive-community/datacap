<template>
  <div :style="{width: width, height: height, padding: '0'}"
       :id="id">
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import * as echarts from 'echarts';
import {v4 as uuidv4} from 'uuid';
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
  data()
  {
    return {
      id: null
    }
  },
  methods: {
    handlerInitialize()
    {
      this.id = uuidv4();
      setTimeout(() => {
        const echartsContainer = document.getElementById(this.id);
        const echartsChart = echarts.init(echartsContainer);
        echartsChart.setOption(this.configure);
      }, 0)
    }
  }
});
</script>
