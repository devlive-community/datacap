<template>
  <div @drop="onDrop">
    <Layout style="min-height: 500px; background-color: #ffffff;">
      <Sider style="background-color: #ffffff; margin: 0px 1px;">
        <FlowSider :data="data"/>
      </Sider>
      <Content>
        <VueFlow :default-viewport="{ zoom: 1.5 }"
                 :min-zoom="0.2"
                 :max-zoom="4"
                 @dragover="onDragOver"
                 @nodeDoubleClick="handlerNodeDoubleClick($event, true)">
          <Background pattern-color="#aaa"
                      gap="8">
          </Background>
          <Controls/>
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
                        @click="saveConfigure(configure)">
                  <FontAwesomeIcon icon="save"/>
                </Button>
              </Tooltip>
            </Space>
          </Panel>
        </VueFlow>
      </Content>
    </Layout>
    <FlowConfigure v-if="configureVisible"
                   :isVisible="configureVisible"
                   :data="contextData"
                   @onChange="handlerChangeConfigure"
                   @close="handlerNodeDoubleClick(null, false)">
    </FlowConfigure>
  </div>
</template>
<script lang="ts">
import {defineComponent, nextTick, watch} from 'vue';
import {Panel, Position, useVueFlow, VueFlow} from '@vue-flow/core';
import {Background} from "@vue-flow/background";
import {Controls} from '@vue-flow/controls'
import FlowSider from "@/components/editor/flow/components/FlowSider.vue";
import FlowConfigure from "@/components/editor/flow/components/FlowConfigure.vue";
import {v4 as uuidv4} from 'uuid';
import {Configuration} from "@/components/editor/flow/Configuration";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {Message} from "view-ui-plus";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'FlowEditor',
  props: {
    data: {
      type: Array as () => Configuration[]
    }
  },
  components: {FontAwesomeIcon, FlowConfigure, FlowSider, VueFlow, Background, Controls, Panel},
  setup(props, {emit})
  {
    const i18n = useI18n();
    const {findNode, onConnect, addEdges, addNodes, project, vueFlowRef, setTransform, toObject} = useVueFlow({nodes: []})

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
      const data = JSON.parse(event.dataTransfer?.getData('application/vueflow'));
      const type = data?.type
      const {left, top} = vueFlowRef.value.getBoundingClientRect();
      const position = project({x: event.clientX - left, y: event.clientY - top});
      const newNode = {id: `${uuidv4()}`, type, position, label: `${data.configure.name}`, data: data.configure}
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

    const resetTransform = () => {
      return setTransform({x: 0, y: 0, zoom: 1});
    }

    const saveConfigure = (configure: any) => {
      const data = toObject();
      if (!configure.from) {
        Message.error(i18n.t('pipeline.pipelineFromValidator'))
        return
      }

      if (!configure.to) {
        Message.error(i18n.t('pipeline.pipelineToValidator'))
        return
      }

      if (data.edges.length === 0) {
        Message.error(i18n.t('pipeline.pipelineEdgeValidator'))
        return
      }
      configure.flow = data;
      emit('onCommit', configure);
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
      configure: {executor: 'Seatunnel', from: null, to: null, flow: null}
    }
  },
  methods: {
    handlerNodeDoubleClick(event: any, isOpen: boolean)
    {
      this.configureVisible = isOpen
      this.contextData = event?.node?.data;
    },
    handlerChangeConfigure(value: any)
    {
      if (value.type === 'input') {
        this.configure.from = value
      }
      else if (value.type === 'output') {
        this.configure.to = value
      }
      this.handlerNodeDoubleClick(null, false)
    }
  },
});
</script>
