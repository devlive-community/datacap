<template>
  <CircularLoading v-if="loading" :show="loading"/>
  <AceEditor v-else :value="statement as string" :read-only="true" :height="'400px'"/>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { SqlType, TableFilter, TableFilterRequest } from '@/model/table.ts'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import TableService from '@/services/table.ts'

export default defineComponent({
  name: 'SourceTableStatement',
  components: {
    AceEditor,
    CircularLoading
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
    this.watchChange()
  },
  methods: {
    handlerInitialize()
    {
      const code = this.$route?.params.table as string
      if (code) {
        this.formState = TableFilterRequest.of()
        this.formState.type = SqlType.SHOW
        this.loading = true
        TableService.getData(code, this.formState)
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
