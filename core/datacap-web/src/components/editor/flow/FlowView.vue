<template>
  <div>
    <div style="height: 500px;">
      <VueFlow v-model="flowData.elements"
               :default-viewport="flowData.transform"
               :min-zoom="0.2"
               :max-zoom="4">
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
import {Configuration} from "@/components/editor/flow/Configuration";

export default defineComponent({
  name: 'FlowView',
  props: {
    flowData: {
      type: Configuration
    }
  },
  components: {VueFlow, Background}
});
</script>
