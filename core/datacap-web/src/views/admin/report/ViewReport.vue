<template>
  <div>
    <Modal v-model="visible"
           width="70%"
           :title="data.name"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <EchartsPreview v-if="data.type === 'QUERY'"
                      :width="'100%'"
                      :height="'300px'"
                      :id="data.id"
                      :configure="JSON.parse(data.configure)">
      </EchartsPreview>
      <VisualView v-else-if="data.type === 'DATASET'"
                  :code="data.dataset.code"
                  :configuration="JSON.parse(data.configure)"
                  :query="JSON.parse(data.query)">
      </VisualView>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, PropType} from "vue";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";
import VisualView from "@/components/visual/VisualView.vue";

interface Data
{
  id: number
  name: string
  type: string
  configure: string,
  query: string,
  dataset: { code: string }
}

export default defineComponent({
  name: "ViewReport",
  components: {VisualView, EchartsPreview},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as unknown as PropType<Data>
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
