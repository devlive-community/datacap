<template>
  <div>
    <div ref="content"
         :style="{width: width, height: height}">
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration"
import VChart from '@visactor/vchart'
import {cloneDeep} from "lodash";

let instance: VChart

export default {
  name: 'VisualBar',
  props: {
    configuration: {
      type: Configuration
    },
    submitted: {
      type: Boolean,
      default: true
    },
    width: {
      type: String,
      default: () => '100%'
    },
    height: {
      type: String,
      default: () => '400px'
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
        try {
          const options = {
            type: 'bar',
            data: {values: this.configuration.columns},
            xField: this.configuration.chartConfigure.xAxis,
            yField: this.configuration.chartConfigure.yAxis
          }
          if (!reset) {
            instance = new VChart(options, {dom: this.$refs.content})
            instance.renderAsync()
          }
          else {
            instance.updateSpec(options, true)
          }
          if (this.submitted) {
            const cloneOptions = cloneDeep(this.configuration)
            cloneOptions.headers = []
            cloneOptions.columns = []
            this.$emit('commitOptions', cloneOptions)
          }
        }
        catch (e) {
          console.warn(e)
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
