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
import ColumnService from '@/services/column'
import { StructureModel } from '@/model/structure.ts'
import { toNumber } from 'lodash'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import ErDiagram from '@/views/components/diagram/ErDiagram.vue'
import { ErDiagramOptions } from '@/views/components/diagram/ErDiagramOptions'

export default defineComponent({
  name: 'TableErDiagram',
  components: { ErDiagram, CircularLoading },
  props: {
    info: {
      type: Object as () => StructureModel | null
    }
  },
  created()
  {
    this.handlerInitialize()
    this.watchId()
  },
  data()
  {
    return {
      loading: false,
      options: null as unknown as ErDiagramOptions
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      if (this.info) {
        ColumnService.getAllByTable(toNumber(this.info.applyId))
                     .then(response => {
                       if (response.status) {
                         this.options = new ErDiagramOptions()
                         this.options.table = { id: toNumber(this.info?.applyId), name: this.info?.title as string }
                         this.options.columns = response.data
                       }
                     })
                     .finally(() => this.loading = false)
      }
    },
    watchId()
    {
      watch(
          () => this.info,
          () => {
            this.handlerInitialize()
          }
      )
    }
  }
})
</script>
