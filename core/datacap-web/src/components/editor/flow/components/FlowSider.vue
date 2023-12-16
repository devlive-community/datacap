<template>
  <div>
    <Card v-for="node in data"
          :key="node.id"
          dis-hover>
      <template #title>
        {{ $t('pipeline.' + node.nodeType) }}
      </template>
      <div class="dndflow">
        <div class="nodes">
          <div :class="'vue-flow__node-' + node.nodeType"
               :draggable="true"
               @dragstart="onDragStart($event, node.nodeType, node)">
            <Avatar size="small"
                    :src="'/static/images/plugin/' + node.type.split(' ')[0] + '.png'">
            </Avatar>
            {{ node.name }}
          </div>
        </div>
      </div>
    </Card>
  </div>
</template>
<script lang="ts">
import '../style.css'

import {defineComponent} from 'vue';
import {Configuration} from "@/components/editor/flow/Configuration";

export default defineComponent({
  name: 'FlowSider',
  props: {
    data: {
      type: Array as () => Configuration[]
    }
  },
  setup()
  {
    /**
     * Sets the data type and value to be transferred during drag start event.
     *
     * @param {object} event - The drag start event object.
     * @param {any} nodeType - The type of node being dragged.
     */
    const onDragStart = (event: { dataTransfer: { setData: (arg0: string, arg1: any) => void; effectAllowed: string; }; }, nodeType: any, configure: any) => {
      if (event.dataTransfer) {
        const data = {type: nodeType, configure: configure}
        event.dataTransfer.setData('application/vueflow', JSON.stringify(data))
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
