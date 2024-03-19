<template>
  <div>
    <NodeResizer :min-width="100" :min-height="30"/>
    <EchartsPreview :width="'250px'" :height="'200px'" :id="id" :configure="configure"/>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import { NodeResizer } from '@vue-flow/node-resizer'
import EchartsPreview from '@/views/components/echarts/EchartsPreview.vue'
import { ChartConfigure } from '@/views/components/echarts/configure/ChartConfigure'

export default defineComponent({
  name: 'DashboardNode',
  components: {NodeResizer, EchartsPreview},
  props: {
    configure: {
      type: Object as () => ChartConfigure | null
    },
    id: {
      type: Number
    }
  }
});
</script>
