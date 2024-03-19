<template>
  <div class="w-full h-full" :style="{width: width + 'px', height: height + 'px'}">
    <VueFlow :default-viewport="{ zoom: 1.5 }" :min-zoom="0.2" :max-zoom="4" @dragover="onDragOver">
      <template #node-resizable="{ data }">
        <DashboardNode :configure="JSON.parse(data.configure)" :id="data.id"/>
      </template>
      <Controls/>
      <Background/>
      <Panel position="top-right">
        <Space>
          <Tooltip :content="$t('pipeline.resetTransform')">
            <Button type="primary"
                    shape="circle"
                    size="small"
                    @click="resetTransform">
              <FontAwesomeIcon icon="rotate"/>
            </Button>
          </Tooltip>
          <Tooltip :content="$t('common.save')">
            <Button type="primary"
                    shape="circle"
                    size="small"
                    @click="saveConfigure(configure, true)">
              <FontAwesomeIcon icon="save"/>
            </Button>
          </Tooltip>
        </Space>
      </Panel>
    </VueFlow>
  </div>
</template>
<script lang="ts">
import { defineComponent, nextTick, ref, watch } from 'vue'
import { Panel, useVueFlow, VueFlow } from '@vue-flow/core'
import { Controls } from '@vue-flow/controls'
import { v4 as uuidv4 } from 'uuid'
import { Background } from '@vue-flow/background'
import DashboardNode from '@/views/pages/admin/dashboard/components/DashboardNode.vue'
import DashboardChart from '@/views/pages/admin/dashboard/components/DashboardChart.vue'
import { DashboardModel } from '@/model/dashboard'

interface ReportNode
{
  id: number
}

export default defineComponent({
  name: 'DashboardEditor',
  components: {
    DashboardNode, DashboardChart,
    Background, VueFlow, Controls, Panel
  },
  props: {
    elements: {
      type: Array,
      default: () => ref([])
    },
    sourceConfigure: {
      type: Object as () => DashboardModel | null
    }
  },
  setup(props, {emit})
  {
    const configureVisible = ref(false);
    const configure = ref({
      id: props.sourceConfigure ? props.sourceConfigure.id : null,
      name: props.sourceConfigure ? props.sourceConfigure.name : null,
      configure: null,
      version: 'V1',
      reports: [] as ReportNode[]
    });

    const {findNode, onConnect, addEdges, addNodes, project, vueFlowRef, setTransform, toObject} = useVueFlow({nodes: []})

    props.elements.forEach((item: any) => {
      const newNode = {id: item.id, position: item.position, label: item.label, type: 'resizable', data: item.data}
      addNodes([newNode])
    })

    onConnect((params: any) => addEdges(params))

    const onDrop = (event: { dataTransfer: { getData: (arg0: string) => any; }; clientX: number; clientY: number; }) => {
      const data = JSON.parse(event.dataTransfer?.getData('application/vueflow'))
      const {left, top} = (vueFlowRef.value as any).getBoundingClientRect()
      const position = project({x: event.clientX - left, y: event.clientY - top})
      const newNode = {id: `${uuidv4()}`, position, label: `${data.name}`, type: 'resizable', data: data}
      addNodes([newNode])
      nextTick(() => {
        const node = findNode(newNode.id) as any
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

    const onDragOver = (event: any) => {
      event.preventDefault()
      if (event.dataTransfer) {
        event.dataTransfer.dropEffect = 'move'
      }
    }

    const resetTransform = () => {
      return setTransform({x: 0, y: 0, zoom: 1});
    }

    const saveConfigure = (configure: any, opened: boolean) => {
      configureVisible.value = opened
      if (!opened) {
        const obj = toObject()
        configure.configure = JSON.stringify(obj)
        obj.nodes.forEach((item) => {
          const node: ReportNode = {id: item.data.id}
          configure.reports.push(node)
        })
        emit('onCommit', configure)
      }
    }

    const height = window.innerHeight - 120
    const width = window.innerWidth - 35

    return {
      onDrop,
      onDragOver,
      resetTransform,
      saveConfigure,
      configure,
      configureVisible,
      height,
      width
    }
  }
});
</script>
