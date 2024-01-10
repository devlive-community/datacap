<template>
  <div>
    <div id="content"
         style="width: 100%; height:400px;">
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration"
import VChart from '@visactor/vchart'

let instance: VChart

export default {
  name: 'VisualLine',
  props: {
    configuration: {
      type: Configuration
    }
  },
  watch: {
    configuration: {
      handler: 'handlerReset',
      deep: true
    }
  },
  created()
  {
    this.handlerInitialize(false)
  },
  methods: {
    handlerInitialize(reset: boolean)
    {
      setTimeout(() => {
        const options = {
          type: 'line',
          data: {values: this.configuration.columns},
          xField: this.configuration.chartConfigure.xAxis,
          yField: this.configuration.chartConfigure.yAxis
        }
        if (!reset) {
          instance = new VChart(options, {dom: 'content'})
          instance.renderAsync()
        }
        else {
          instance.updateSpec(options, true)
        }
      })
    },
    handlerReset()
    {
      this.handlerInitialize(true)
    }
  }
}
</script>
