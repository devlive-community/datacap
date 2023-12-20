<template>
  <div>
    <div style="height: 150px;">
      <VueFlow v-model="configure.nodes"
               fit-view-on-init
               :default-viewport="configure.viewport"
               :min-zoom="0.2"
               :nodes-draggable="false"
               :snap-to-grid="true"
               :select-nodes-on-drag="false"
               :pan-on-drag="false"
               :max-zoom="4">
        <template #node-resizable="{ data }">
          <DashboardNode :configure="JSON.parse(data.configure)"/>
        </template>
        <Background pattern-color="#aaa"
                    gap="8">
        </Background>
      </VueFlow>
    </div>
  </div>
</template>
<script lang="ts">
import './style.css';
import {defineComponent} from 'vue';
import {VueFlow} from '@vue-flow/core';
import {Background} from "@vue-flow/background";
import DashboardNode from "@/components/editor/dashboard/components/DashboardNode.vue";

export default defineComponent({
  props: {
    configure: {
      type: Object
    }
  },
  components: {DashboardNode, VueFlow, Background}
});
</script>
