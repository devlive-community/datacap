<template>
  <div>
    <Card dis-hover>
      <template #title>
        {{ $t('pipeline.input') }}
      </template>
      <div class="dndflow">
        <div class="nodes">
          <div class="vue-flow__node-input"
               :draggable="true"
               @dragstart="onDragStart($event, 'input')">
            {{ $t('pipeline.input') }} Node
          </div>
        </div>
      </div>
    </Card>
    <Card dis-hover>
      <template #title>
        {{ $t('pipeline.output') }}
      </template>
      <div class="dndflow">
        <div class="nodes">
          <div class="vue-flow__node-output"
               :draggable="true"
               @dragstart="onDragStart($event, 'output')">
            {{ $t('pipeline.output') }} Node
          </div>
        </div>
      </div>
    </Card>
  </div>
</template>
<script lang="ts">
import '../style.css'

import {defineComponent} from 'vue';

export default defineComponent({
  name: 'FlowSider',
  setup()
  {
    /**
     * Sets the data type and value to be transferred during drag start event.
     *
     * @param {object} event - The drag start event object.
     * @param {any} nodeType - The type of node being dragged.
     */
    const onDragStart = (event: { dataTransfer: { setData: (arg0: string, arg1: any) => void; effectAllowed: string; }; }, nodeType: any) => {
      if (event.dataTransfer) {
        event.dataTransfer.setData('application/vueflow', nodeType)
        event.dataTransfer.effectAllowed = 'move'
      }
    }
    return {
      onDragStart
    }
  }
});
</script>
<style scoped>
:deep(.ivu-card-head) {
  text-align: center;
  font-weight: bold;
}
</style>
