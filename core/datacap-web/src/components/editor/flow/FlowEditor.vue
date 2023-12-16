<template>
  <div @drop="onDrop">
    <Layout style="min-height: 500px; background-color: #ffffff;">
      <Sider style="background-color: #ffffff; margin: 0px 1px;">
        <FlowSider></FlowSider>
      </Sider>
      <Content>
        <VueFlow :default-viewport="{ zoom: 1.5 }"
                 :min-zoom="0.2"
                 :max-zoom="4"
                 @dragover="onDragOver"
                 @nodeDoubleClick="handlerNodeDoubleClick">
          <Background pattern-color="#aaa"
                      gap="8">
          </Background>
        </VueFlow>
      </Content>
    </Layout>
  </div>
</template>
<script lang="ts">
import {defineComponent, nextTick, watch} from 'vue';
import {Position, useVueFlow, VueFlow} from '@vue-flow/core';
import {Background} from "@vue-flow/background";
import FlowSider from "@/components/editor/flow/components/FlowSider.vue";
import {v4 as uuidv4} from 'uuid';

export default defineComponent({
  name: 'FlowEditor',
  props: {
    nodes: {
      type: [],
      default: () => [],
    }
  },
  components: {FlowSider, VueFlow, Background},
  setup(props)
  {
    const {findNode, onConnect, addEdges, addNodes, project, vueFlowRef} = useVueFlow({nodes: props.nodes})

    onConnect((params) => addEdges(params))

    /**
     * Handles the drop event.
     *
     * @param {Object} event - The drop event object.
     * @param {Object} event.dataTransfer - The dataTransfer object.
     * @param {Function} event.dataTransfer.getData - A function to get data from the dataTransfer object.
     * @param {number} event.clientX - The x-coordinate of the cursor position.
     * @param {number} event.clientY - The y-coordinate of the cursor position.
     */
    const onDrop = (event: { dataTransfer: { getData: (arg0: string) => any; }; clientX: number; clientY: number; }) => {
      const type = event.dataTransfer?.getData('application/vueflow');
      const {left, top} = vueFlowRef.value.getBoundingClientRect();
      const position = project({x: event.clientX - left, y: event.clientY - top});
      const newNode = {id: `${uuidv4()}`, type, position, label: `${type} node`}
      if (type === 'input') {
        newNode['sourcePosition'] = Position.Right;
      }
      else if (type === 'output') {
        newNode['targetPosition'] = Position.Left;
      }
      addNodes([newNode])
      // align node position after drop, so it's centered to the mouse
      nextTick(() => {
        const node = findNode(newNode.id)
        const stop = watch(
          () => node.dimensions,
          (dimensions) => {
            if (dimensions.width > 0 && dimensions.height > 0) {
              node.position = {x: node.position.x - node.dimensions.width / 2, y: node.position.y - node.dimensions.height / 2}
              stop()
            }
          },
          {deep: true, flush: 'post'}
        )
      })
    }

    /**
     * Handles the drag over event.
     *
     * @param {object} event - The drag over event.
     * @param {function} event.preventDefault - Prevents the default behavior of the drag over event.
     * @param {object} event.dataTransfer - The data transfer object of the drag over event.
     * @param {string} event.dataTransfer.dropEffect - The drop effect of the drag over event.
     */
    const onDragOver = (event: { preventDefault: () => void; dataTransfer: { dropEffect: string; }; }) => {
      event.preventDefault()
      if (event.dataTransfer) {
        event.dataTransfer.dropEffect = 'move'
      }
    }
    return {
      onDrop,
      onDragOver
    }
  },
  methods: {
    handlerNodeDoubleClick()
    {
      console.log('Double click');
    }
  },
});
</script>
