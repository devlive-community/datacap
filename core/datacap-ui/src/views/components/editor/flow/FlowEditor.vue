<template>
  <div @drop="onDrop">
    <div class="hidden space-y-6 pb-16 md:block">
      <div class="flex flex-col space-y-8 h-screen lg:flex-row lg:space-x-6 lg:space-y-0">
        <aside class="-mx-4 w-[165px]">
          <FlowSider :data="data"/>
        </aside>
        <div class="flex-1">
          <VueFlow :default-viewport="{ zoom: 1.5 }" :min-zoom="0.2" :max-zoom="4" @dragover="onDragOver" @nodeClick="handlerNodeClick($event, true)">
            <Background pattern-color="#aaa" :gap="8"/>
            <Controls/>
            <Panel position="top-right">
              <div class="space-x-2">
                <Tooltip :content="$t('pipeline.common.resetTransform')">
                  <Button size="icon" class="w-6 h-6 rounded-full" @click="resetTransform">
                    <RefreshCcw :size="16"/>
                  </Button>
                </Tooltip>
                <Tooltip :content="$t('common.save')">
                  <Button size="icon" class="w-6 h-6 rounded-full" @click="saveConfigure(configure)">
                    <Save :size="16"/>
                  </Button>
                </Tooltip>
              </div>
            </Panel>
          </VueFlow>
        </div>
      </div>
    </div>
    <FlowConfigure v-if="configureVisible" :isVisible="configureVisible" :data="contextData" @onChange="handlerChangeConfigure" @close="handlerNodeClick(null, false)"/>
  </div>
</template>
<script lang="ts">
import { defineComponent, nextTick, watch } from 'vue'
import { Panel, Position, useVueFlow, VueFlow } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { v4 as uuidv4 } from 'uuid'
import { useI18n } from 'vue-i18n'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'
import { ToastUtils } from '@/utils/toast.ts'
import FlowSider from '@/views/components/editor/flow/components/FlowSider.vue'
import { RefreshCcw, Save } from 'lucide-vue-next'
import Button from '@/views/ui/button'
import Tooltip from '@/views/ui/tooltip'
import FlowConfigure from '@/views/components/editor/flow/components/FlowConfigure.vue'

export default defineComponent({
  name: 'FlowEditor',
  props: {
    data: {
      type: Array as () => Configuration[]
    }
  },
  components: {
    FlowConfigure,
    FlowSider,
    VueFlow, Background, Controls, Panel,
    Save, RefreshCcw,
    Button,
    Tooltip
  },
  setup(props, { emit })
  {
    console.debug(props.data)

    const i18n = useI18n()
    const { findNode, onConnect, addEdges, addNodes, project, vueFlowRef, setTransform, toObject } = useVueFlow({ nodes: [] })

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
    const onDrop = (event: any) => {
      const data = JSON.parse(event.dataTransfer?.getData('application/vueflow'))
      const type = data?.type
      const { left, top } = (vueFlowRef as any).value.getBoundingClientRect()
      const position = project({ x: event.clientX - left, y: event.clientY - top })
      const newNode = { id: `${ uuidv4() }`, type, position, label: `${ data.configure.name }`, data: data.configure } as any
      if (type === 'input') {
        newNode['sourcePosition'] = Position.Right
      }
      else if (type === 'output') {
        newNode['targetPosition'] = Position.Left
      }
      addNodes([newNode])
      // align node position after drop, so it's centered to the mouse
      nextTick(() => {
        const node = findNode(newNode.id) as any
        const stop = watch(
            () => node.dimensions,
            (dimensions) => {
              if (dimensions.width > 0 && dimensions.height > 0) {
                node.position = { x: node.position.x - node.dimensions.width / 2, y: node.position.y - node.dimensions.height / 2 }
                stop()
              }
            },
            { deep: true, flush: 'post' }
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
    const onDragOver = (event: any) => {
      event.preventDefault()
      if (event.dataTransfer) {
        event.dataTransfer.dropEffect = 'move'
      }
    }

    const resetTransform = () => {
      return setTransform({ x: 0, y: 0, zoom: 1 })
    }

    const saveConfigure = (configure: any) => {
      const data = toObject()
      if (!configure.from) {
        ToastUtils.error(i18n.t('pipeline.validator.from'))
        return
      }

      if (!configure.to) {
        ToastUtils.error(i18n.t('pipeline.validator.to'))
        return
      }

      if (data.edges.length === 0) {
        ToastUtils.error(i18n.t('pipeline.validator.edge'))
        return
      }
      configure.flow = data
      emit('onCommit', configure)
    }

    return {
      onDrop,
      onDragOver,
      resetTransform,
      saveConfigure
    }
  },
  data()
  {
    return {
      configureVisible: false,
      contextData: null,
      configure: { executor: 'Seatunnel', from: null, to: null, flow: null }
    }
  },
  methods: {
    handlerNodeClick(event: any, isOpen: boolean)
    {
      this.configureVisible = isOpen
      this.contextData = event?.node?.data
    },
    handlerChangeConfigure(value: any)
    {
      if (value.type === 'input') {
        this.configure.from = value
      }
      else if (value.type === 'output') {
        this.configure.to = value
      }
      this.handlerNodeClick(null, false)
    }
  }
})
</script>
