<template>
  <div>
    <NodeResizer min-width="100" min-height="30">
    </NodeResizer>
    <EchartsPreview :width="'250px'"
                    :height="'200px'"
                    :configure="configure">
    </EchartsPreview>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {NodeResizer} from '@vue-flow/node-resizer'
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";

export default defineComponent({
  name: 'DashboardNode',
  components: {NodeResizer, EchartsPreview},
  props: {
    configure: {
      type: Object
    }
  }
});
</script>
