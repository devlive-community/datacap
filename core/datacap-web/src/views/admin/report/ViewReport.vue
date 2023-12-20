<template>
  <div>
    <Modal v-model="visible"
           :title="data.name"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <EchartsPreview :width="'100%'"
                      :height="'300px'"
                      :id="data.id"
                      :configure="JSON.parse(data.configure)">
      </EchartsPreview>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";

export default defineComponent({
  name: "ViewReport",
  components: {EchartsPreview},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object
    }
  },
  data()
  {
    return {
      loading: false
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
<style scoped>
:deep(.ivu-modal-body) {
  padding: 0;
}
</style>
