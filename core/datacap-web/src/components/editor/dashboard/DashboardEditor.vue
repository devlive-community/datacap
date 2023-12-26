<template>
  <div @drop="onDrop">
    <Layout style="min-height: 500px; background-color: #ffffff;">
      <Sider style="background-color: #ffffff; margin: 0px 1px;">
        <DashboardChart/>
      </Sider>
      <Content>
        <VueFlow :default-viewport="{ zoom: 1.5 }"
                 :min-zoom="0.2"
                 :max-zoom="4"
                 @dragover="onDragOver">
          <template #node-resizable="{ data }">
            <DashboardNode :configure="JSON.parse(data.configure)"
                           :id="data.id">
            </DashboardNode>
          </template>
          <Controls/>
          <Background>
          </Background>
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
      </Content>
    </Layout>
    <Modal v-model="configureVisible"
           :title="$t('common.configure')">
      <Form>
        <FormItem :label="$t('common.name')"
                  :label-width="80">
          <Input v-model="configure.name"/>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary"
                @click="saveConfigure(configure, false)">
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, nextTick, ref, watch} from 'vue';
import {Panel, useVueFlow, VueFlow} from '@vue-flow/core';
import {Controls} from '@vue-flow/controls'
import {v4 as uuidv4} from 'uuid';
import DashboardChart from "@/components/editor/dashboard/components/DashboardChart.vue";
import DashboardNode from "@/components/editor/dashboard/components/DashboardNode.vue";
import {Background} from "@vue-flow/background";

export default defineComponent({
  name: 'DashboardEditor',
  components: {Background, DashboardNode, DashboardChart, VueFlow, Controls, Panel},
  props: {
    elements: {
      type: Array,
      default: () => ref([])
    },
    sourceConfigure: {
      type: Object
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
      reports: []
    });

    const {findNode, onConnect, addEdges, addNodes, project, vueFlowRef, setTransform, toObject} = useVueFlow({nodes: []})

    props.elements.forEach((item: { id: any, position: any, label: any, type: any, data: any }) => {
      const newNode = {id: item.id, position: item.position, label: item.label, type: 'resizable', data: item.data}
      addNodes([newNode])
    })

    onConnect((params) => addEdges(params))

    const onDrop = (event: { dataTransfer: { getData: (arg0: string) => any; }; clientX: number; clientY: number; }) => {
      const data = JSON.parse(event.dataTransfer?.getData('application/vueflow'));
      const {left, top} = vueFlowRef.value.getBoundingClientRect();
      const position = project({x: event.clientX - left, y: event.clientY - top});
      const newNode = {id: `${uuidv4()}`, position, label: `${data.name}`, type: 'resizable', data: data}
      addNodes([newNode])
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

    const onDragOver = (event: { preventDefault: () => void; dataTransfer: { dropEffect: string; }; }) => {
      event.preventDefault()
      if (event.dataTransfer) {
        event.dataTransfer.dropEffect = 'move'
      }
    }

    const resetTransform = () => {
      return setTransform({x: 0, y: 0, zoom: 1});
    }

    const saveConfigure = (configure: any, opened: boolean) => {
      configureVisible.value = opened;
      if (!opened) {
        const obj = toObject()
        configure.configure = JSON.stringify(obj);
        obj.nodes.forEach(item => {
          configure.reports.push({id: item.data.id})
        })
        emit('onCommit', configure);
      }
    }
    return {
      onDrop,
      onDragOver,
      resetTransform,
      saveConfigure,
      configure,
      configureVisible
    }
  }
});
</script>
