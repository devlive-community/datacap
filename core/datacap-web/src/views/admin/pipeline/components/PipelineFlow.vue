<template>
  <div>
    <Modal v-model="visible"
           :title="$t('pipeline.flow')"
           :mask-closable="false"
           :width="'80%'"
           @cancel="handlerCancel()">
      <FlowView :flowData="flowData"/>
      <template #footer>
        <Button @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import FlowView from "@/components/editor/flow/FlowView.vue";
import {Configuration} from "@/components/editor/flow/Configuration";

export default defineComponent({
  name: "PipelineFlow",
  components: {FlowView},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    flowData: {
      type: Configuration
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false;
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    }
  }
});
</script>
