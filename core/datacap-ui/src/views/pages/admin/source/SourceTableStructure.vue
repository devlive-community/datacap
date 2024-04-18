<template>
  <div class="h-full">
    <TableCommon :loading="loading" :columns="headers" :data="data">
      <template #isNullable="{row}">
        <Switch :value="row.isNullable" disabled="disabled"/>
      </template>
    </TableCommon>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { createHeaders } from '@/views/pages/admin/source/components/TableUtils.ts'
import TableCommon from '@/views/components/table/TableCommon.vue'
import ColumnService from '@/services/column.ts'
import { ColumnModel } from '@/model/column.ts'
import Switch from '@/views/ui/switch'

export default defineComponent({
  name: 'SourceTableStructure',
  components: {
    TableCommon,
    Switch
  },
  setup()
  {
    const headers = createHeaders(useI18n())

    return {
      headers
    }
  },
  data()
  {
    return {
      loading: false,
      data: Array<ColumnModel>
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
                       if (response.status) {
                         this.data = response.data
                       }
                     })
                     .finally(() => this.loading = false)
      }
    },
    watchChange()
    {
      watch(
          () => this.$route?.params.table,
          () => {
            this.handlerInitialize()
          }
      )
    }
  }
})
</script>
