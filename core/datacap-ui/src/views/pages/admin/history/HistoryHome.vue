<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('common.history') }}</template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #plugin="{ row }">
          <span>{{ row?.plugin?.name }}</span>
        </template>
        <template #type="{row}">
          <Tooltip :content="row?.plugin?.type">
            <Avatar :size="'sm'" :src="'/static/images/plugin/' + row?.plugin?.type + '.png'" :alt="row?.plugin?.type" class="cursor-pointer"/>
          </Tooltip>
        </template>
        <template #mode="{ row }">
          <Tag v-if="row.mode === 'ADHOC'">{{ $t('common.adhoc') }}</Tag>
          <Tag v-else-if="row.mode === 'HISTORY'">{{ $t('common.history') }}</Tag>
          <Tag v-else-if="row.mode === 'REPORT'">{{ $t('common.report') }}</Tag>
          <Tag v-else-if="row.mode === 'SNIPPET'">{{ $t('common.snippet') }}</Tag>
        </template>
        <template #state="{ row }">
          <Tag :class="row.state === 'SUCCESS' ? '' : 'bg-color-error'">{{ row.state }}</Tag>
        </template>
      </TableCommon>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { Pencil } from 'lucide-vue-next'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { FilterModel } from '@/model/filter.ts'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import AuditService from '@/services/audit'
import { createHeaders } from '@/views/pages/admin/history/HistoryUtils'
import Tooltip from '@/views/ui/tooltip'
import Avatar from '@/views/ui/avatar'
import Tag from '@/views/ui/tag'

export default defineComponent({
  name: 'HistoryHome',
  components: {
    TableCommon,
    Pencil,
    Card,
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
      AuditService.getPluginAudits(this.filter)
                  .then((response) => {
                    if (response.status) {
                      this.data = response.data.content
                      this.pagination = PaginationRequest.of(response.data)
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
