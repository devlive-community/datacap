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
        <template #action="{row}">
          <div class="space-x-2">
            <Tooltip :content="$t('common.error')">
              <Button :disabled="row.state === 'SUCCESS'" :color="'#ed4014'" size="icon" class="w-6 h-6 rounded-full" @click="handlerShowContent(true, row?.message)">
                <TriangleAlert :size="14"/>
              </Button>
            </Tooltip>
          </div>
        </template>
      </TableCommon>
    </Card>
  </div>
  <SqlInfo v-if="contentVisible" :is-visible="contentVisible" :content="content as string" @close="handlerShowContent(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { Pencil, TriangleAlert } from 'lucide-vue-next'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { FilterModel } from '@/model/filter.ts'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import AuditService from '@/services/audit'
import { createHeaders } from '@/views/pages/admin/history/HistoryUtils'
import Tooltip from '@/views/ui/tooltip'
import Avatar from '@/views/ui/avatar'
import Tag from '@/views/ui/tag'
import Button from '@/views/ui/button'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'

export default defineComponent({
  name: 'HistoryHome',
  components: {
    SqlInfo,
    Button,
    TableCommon,
    Pencil,
    Card,
    Tooltip,
    Avatar,
    Tag,
    TriangleAlert
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
      pagination: {} as PaginationModel,
      contentVisible: false,
      content: null as string | null
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
    },
    handlerShowContent(opened: boolean, content: string | null)
    {
      this.contentVisible = opened
      this.content = content
    }
  }
})
</script>
