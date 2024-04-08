<template>
  <TableCommon :loading="loading" :columns="headers" :data="data">
    <template #isNullable="{row}">
      <Switch :value="row.isNullable" disabled="disabled"/>
    </template>
  </TableCommon>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { StructureModel } from '@/model/structure'
import { useI18n } from 'vue-i18n'
import { createHeaders } from '@/views/pages/admin/source/components/TableUtils'
import TableCommon from '@/views/components/table/TableCommon.vue'
import ColumnService from '@/services/column'
import { cloneDeep, toNumber } from 'lodash'
import { TableModel } from '@/model/table'
import { ColumnModel } from '@/model/column'
import Switch from '@/views/ui/switch'

export default defineComponent({
  name: 'TableStructure',
  components: {
    TableCommon,
    Switch
  },
  props: {
    info: {
      type: Object as () => StructureModel | null
    }
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
      dataInfo: null as TableModel | null,
      data: Array<ColumnModel>
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
        this.dataInfo = cloneDeep(this.info.origin)
        this.loading = true
        ColumnService.getAllByTable(toNumber(this.dataInfo?.id))
                     .then(response => {
                       if (response.status) {
                         this.data = response.data
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
