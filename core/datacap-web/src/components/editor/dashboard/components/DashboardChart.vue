<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <Card v-else
          dis-hover
          style="margin-top: 3px;"
          v-for="node in data"
          padding="0"
          :key="node.id">
      <template #title>
        <div style="text-align: center;">
          {{ node.name }}
        </div>
      </template>
      <div :draggable="true"
           @dragstart="onDragStart($event, node)">
        <EchartsPreview :key="node.id"
                        :height="'200px'"
                        :configure="JSON.parse(node.configure)">
        </EchartsPreview>
      </div>
    </Card>
  </div>
</template>
<script lang="ts">
import '../style.css'
import {defineComponent} from 'vue';
import {Filter} from "@/model/Filter";
import ReportService from "@/services/admin/ReportService";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";

export default defineComponent({
  name: 'DashboardChart',
  components: {EchartsPreview, CircularLoading},
  setup()
  {
    const onDragStart = (event: { dataTransfer: { setData: (arg0: string, arg1: any) => void; effectAllowed: string; }; }, node: any) => {
      if (event.dataTransfer) {
        event.dataTransfer.setData('application/vueflow', JSON.stringify(node))
        event.dataTransfer.effectAllowed = 'move'
      }
    }
    return {
      onDragStart
    }
  },
  data()
  {
    return {
      loading: false,
      data: []
    }
  },
  created()
  {
    this.loading = true;
    const filter: Filter = new Filter();
    ReportService.getAll(filter)
      .then((response) => {
        if (response.status) {
          this.data = response.data.content;
        }
      })
      .finally(() => this.loading = false);
  }
});
</script>
<style scoped>
:deep(.ivu-card-head) {
  text-align: center;
  font-weight: bold;
}
</style>
