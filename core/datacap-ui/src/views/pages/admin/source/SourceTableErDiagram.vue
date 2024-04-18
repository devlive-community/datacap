<template>
  <div>
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else>
      <ErDiagram :options="options"/>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, watch } from 'vue'
import ColumnService from '@/services/column.ts'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import ErDiagram from '@/views/components/diagram/ErDiagram.vue'
import { ErDiagramOptions } from '@/views/components/diagram/ErDiagramOptions.ts'

export default defineComponent({
  name: 'SourceTableErDiagram',
  components: { ErDiagram, CircularLoading },
  data()
  {
    return {
      loading: false,
      options: null as unknown as ErDiagramOptions
    }
  },
  created()
  {
    this.handlerInitialize()
    this.watchChange()
  },
  methods: {
    handlerInitialize()
    {
      const code = this.$route?.params.table as string
      if (code) {
        this.loading = true
        ColumnService.getAllByTable(code)
                     .then(response => {
                       if (response.status && response.data?.length > 0) {
                         const table = response.data[0]
                         this.options = new ErDiagramOptions()
                         this.options.table = { id: table.id, name: table.name }
                         this.options.columns = response.data
                       }
                     })
                     .finally(() => this.loading = false)
      }
    },
    watchChange()
    {
      watch(
          () => this.$route?.params.table,
          () => this.handlerInitialize()
      )
    }
  }
})
</script>
