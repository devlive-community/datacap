<template>
  <div>
    <CircularLoading v-if="loading" :show="loading"/>
    <Card v-else class="mt-3" v-for="node in data">
      <CardHeader>
        <CardTitle>{{ node.name }}</CardTitle>
      </CardHeader>
      <CardContent>
        <div :draggable="true" @dragstart="onDragStart($event, node)">
          <EchartsPreview :key="node.id" :height="'200px'" :id="node.id" :configure="JSON.parse(node.configure)"/>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script lang="ts">
import '../style.css'
import { defineComponent } from 'vue'
import ReportService from '@/services/report'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { FilterModel } from '@/model/filter'
import EchartsPreview from '@/views/components/echarts/EchartsPreview.vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

interface Node {
  id: number
  name: string
  configure: string
}

export default defineComponent({
  name: 'DashboardChart',
  components: {
    CardContent, CardHeader, CardTitle, Card,
    EchartsPreview,
    CircularLoading
  },
  setup()
  {
    const onDragStart = (event: any, node: any) => {
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
      data: [] as Node[]
    }
  },
  created()
  {
    this.loading = true
    const filter: FilterModel = new FilterModel()
    ReportService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.data = response.data.content
          }
        })
        .finally(() => this.loading = false)
  }
});
</script>
