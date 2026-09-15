<template>
  <a-layout style="height: calc(100vh - 35px)" @drop="onDrop">
    <a-layout has-sider>
      <a-layout-sider theme="light">
        <FlowSider v-if="data" :data="data"/>
      </a-layout-sider>

      <a-layout-content>
        <VueFlow :default-viewport="{ zoom: 1.5 }"
                 :min-zoom="0.2"
                 :max-zoom="4"
                 @dragover="onDragOver"
                 @nodeClick="onNodeClick($event, true)">
          <Background pattern-color="#aaa" :gap="8"/>
          <Controls/>
          <Panel position="top-right">
            <div class="space-x-2">
              <a-tooltip :title="$t('pipeline.common.resetTransform')">
                <a-button shape="circle" @click="resetTransform">
                  <template #icon>
                    <ShadcnIcon icon="RefreshCcw"/>
                  </template>
                </a-button>
              </a-tooltip>

              <a-tooltip :title="$t('common.save')">
                <a-button shape="circle" @click="saveConfigure(configure)">
                  <template #icon>
                    <ShadcnIcon icon="Save"/>
                  </template>
                </a-button>
              </a-tooltip>
            </div>
          </Panel>
        </VueFlow>
      </a-layout-content>
    </a-layout>
  </a-layout>

  <FlowConfigure v-if="configureVisible"
                 :isVisible="configureVisible"
                 :data="contextData"
                 @onChange="onChangeConfigure"
                 @close="onNodeClick(null, false)"/>
</template>

<script setup lang="ts">
import { nextTick, ref, watch } from 'vue'
import { Panel, Position, useVueFlow, VueFlow } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { v4 as uuidv4 } from 'uuid'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'
import FlowSider from '@/views/components/editor/flow/components/FlowSider.vue'
import FlowConfigure from '@/views/components/editor/flow/components/FlowConfigure.vue'

defineOptions({ name: 'FlowEditor' })

const props = defineProps<{ data?: Configuration[] }>()
const emit = defineEmits<{ (e: 'onCommit', value: any): void }>()

console.debug(props.data)

const i18n = useI18n()
const { findNode, onConnect, addEdges, addNodes, project, vueFlowRef, setTransform, toObject } = useVueFlow({ nodes: [] })

onConnect((params) => addEdges(params))

const configureVisible = ref(false)
const contextData = ref<any>(null)
const configure = ref<any>({ executor: 'Seatunnel', from: null, to: null, flow: null })

/**
 * Handles the drop event.
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

const saveConfigure = (conf: any) => {
  const data = toObject()
  if (!conf.from) {
    message.error(i18n.t('pipeline.validator.from'))
    return
  }

  if (!conf.to) {
    message.error(i18n.t('pipeline.validator.to'))
    return
  }

  if (data.edges.length === 0) {
    message.error(i18n.t('pipeline.validator.edge'))
    return
  }
  conf.flow = data
  emit('onCommit', conf)
}

const onNodeClick = (event: any, isOpen: boolean) => {
  configureVisible.value = isOpen
  contextData.value = event?.node?.data
}

const onChangeConfigure = (value: any) => {
  if (value.type === 'input') {
    configure.value.from = value
  }
  else if (value.type === 'output') {
    configure.value.to = value
  }
  onNodeClick(null, false)
}
</script>
