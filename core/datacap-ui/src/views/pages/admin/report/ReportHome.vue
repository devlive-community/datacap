<template>
  <div class="w-full">
    <Card>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #realtime="{row}">
          <Switch :value="row.realtime" :disabled="true"/>
        </template>
        <template #source="{ row }">
          <Tooltip v-if="row.source" :content="row.source?.type">
            <Avatar :size="'sm'" :src="'/static/images/plugin/' + row.source?.type + '.png'" :alt="row.source?.type"/>
          </Tooltip>
          <Tooltip v-else :content="row.dataset?.name">
            <Tag>{{ $t('common.dataset') }}</Tag>
          </Tooltip>
        </template>
      </TableCommon>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHeaders } from '@/views/pages/admin/report/ReportUtils'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { ToastUtils } from '@/utils/toast'
import ReportService from '@/services/report'
import TableCommon from '@/views/components/table/TableCommon.vue'
import Switch from '@/views/ui/switch'
import Tooltip from '@/views/ui/tooltip'
import Avatar from '@/views/ui/avatar'
import Tag from '@/views/ui/tag'

export default defineComponent({
  name: 'ReportHome',
  components: {
    TableCommon,
    Card,
    Switch,
    Tooltip,
    Avatar,
    Tag
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(useI18n())
    return {
      filter,
      headers
    }
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pagination: {} as PaginationModel
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      ReportService.getAll(this.filter)
                   .then(response => {
                     if (response.status) {
                       this.data = response.data.content
                       this.pagination = PaginationRequest.of(response.data)
                     }
                     else {
                       ToastUtils.error(response.message)
                     }
                   })
                   .finally(() => this.loading = false)
    },
    handlerChangePage(value: PaginationModel)
    {
      this.filter.page = value.currentPage
      this.filter.size = value.pageSize
      this.handlerInitialize()
    }
  }
})
</script>
