<template>
  <div>
    <Modal v-model="visible"
           width="70%"
           :title="`[ ${data.id} ] ${$t('common.data')}`"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <CircularLoading v-if="loading"
                       :show="loading">
      </CircularLoading>
      <div v-else>
        <VisualTable :configuration="localConfiguration"
                     :submitted="false">
        </VisualTable>
      </div>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, PropType} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import HistoryService from "@/services/admin/HistoryService";
import VisualTable from "@/components/visual/components/VisualTable.vue";
import {Configuration} from "@/components/visual/Configuration";

interface Data
{
  id: number
  name: string
}

export default defineComponent({
  name: "HistoryData",
  components: {VisualTable, CircularLoading},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as unknown as PropType<Data>
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      localConfiguration: null as Configuration
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      HistoryService.getData(this.data.id)
        .then(response => {
          if (response.status) {
            this.localConfiguration = new Configuration()
            this.localConfiguration.headers = response.data.headers
            this.localConfiguration.columns = response.data.columns
          }
        })
        .finally(() => this.loading = false)
    },
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
