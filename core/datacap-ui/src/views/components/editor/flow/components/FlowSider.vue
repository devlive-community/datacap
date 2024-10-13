<template>
  <div class="space-y-2">
    <Card :title-class="'p-2'" :body-class="'p-2'">
      <template #title>{{ $t('pipeline.common.input') }}</template>
      <div v-for="node in inputs" :key="node.id" class="dndflow">
        <div class="nodes">
          <div :class="'flex items-center space-x-3 vue-flow__node-' + node.nodeType" :draggable="true" @dragstart="onDragStart($event, node.nodeType, node)">
            <Avatar :src="'/static/images/plugin/' + (node.type as string).split(' ')[0] + '.png'"/>
            <span>{{ node.name }}</span>
          </div>
        </div>
      </div>
    </Card>
    <Card :title-class="'p-2'" :body-class="'p-2'">
      <template #title>{{ $t('pipeline.common.output') }}</template>
      <div v-for="node in outputs" :key="node.id" class="dndflow">
        <div class="nodes">
          <div :class="'flex items-center space-x-3 vue-flow__node-' + node.nodeType" :draggable="true" @dragstart="onDragStart($event, node.nodeType, node)">
            <Avatar :src="'/static/images/plugin/' + (node.type as string).split(' ')[0] + '.png'"/>
            {{ node.name }}
          </div>
        </div>
      </div>
    </Card>
  </div>
</template>
<script lang="ts">
import '../style.css'
import { defineComponent } from 'vue'
import { DataCapCard } from '@/views/ui/card'
import Avatar from '@/views/ui/avatar'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'

export default defineComponent({
  name: 'FlowSider',
  components: {
    Card,
    Avatar
  },
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
    const onDragStart = (event: any, nodeType: any, configure: any) => {
      if (event.dataTransfer) {
        const data = { type: nodeType, configure: configure }
        event.dataTransfer.setData('application/vueflow', JSON.stringify(data))
        event.dataTransfer.effectAllowed = 'move'
      }
    }
    return {
      onDragStart
    }
  },
  data()
  {
    return {
      inputs: [] as Configuration[],
      outputs: [] as Configuration[]
    }
  },
  created()
  {
    if (this.data) {
      this.inputs = this.data.filter(v => v.nodeType === 'input')
      this.outputs = this.data.filter(v => v.nodeType === 'output')
    }
  }
})
</script>
