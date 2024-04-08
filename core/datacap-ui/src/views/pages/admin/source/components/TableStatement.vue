<template>
  <CircularLoading v-if="loading" :show="loading"/>
  <AceEditor v-else :value="statement as string" :read-only="true" :height="'400px'"/>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { StructureModel } from '@/model/structure'
import { toNumber } from 'lodash'
import { SqlType, TableFilter, TableFilterRequest } from '@/model/table'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import TableService from '@/services/table'

export default defineComponent({
  name: 'TableStatement',
  components: {
    AceEditor,
    CircularLoading
  },
  props: {
    info: {
      type: Object as () => StructureModel | null
    }
  },
  data()
  {
    return {
      loading: false,
      statement: null as string | null,
      formState: null as unknown as TableFilter
    }
  },
  created()
  {
    this.handlerInitialize()
    this.watchId()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.formState = TableFilterRequest.of()
        this.formState.type = SqlType.SHOW
        this.loading = true
        TableService.getData(toNumber(this.info.applyId), this.formState)
                    .then(response => {
                      if (response.status) {
                        const content = response.data?.columns[0]
                        if (content.length > 1) {
                          this.statement = content[1]
                        }
                        else {
                          this.statement = content[0]
                        }
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
