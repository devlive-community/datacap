<template>
  <div class="w-full">
    <Card title-class="p-3.5">
      <template #title>{{ $t('common.history') }}</template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #source="{ row }">
          <span>{{ row?.source?.name }}</span>
        </template>
        <template #type="{row}">
          <Tooltip :content="row?.source?.type">
            <Avatar :size="'sm'" :src="'/static/images/plugin/' + row?.source?.type + '.png'" :alt="row?.source?.type" class="cursor-pointer"/>
          </Tooltip>
        </template>
        <template #mode="{ row }">
          <Tag v-if="row.mode === 'ADHOC'">{{ $t('common.adhoc') }}</Tag>
          <Tag v-else-if="row.mode === 'HISTORY'">{{ $t('common.history') }}</Tag>
          <Tag v-else-if="row.mode === 'REPORT'">{{ $t('common.report') }}</Tag>
          <Tag v-else-if="row.mode === 'SNIPPET'">{{ $t('common.snippet') }}</Tag>
          <Tag v-else-if="row.mode === 'DATASET'">{{ $t('common.dataset') }}</Tag>
          <Tag v-else>{{ row.mode }}</Tag>
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
            <DropdownMenu>
              <DropdownMenuTrigger as-child>
                <Button size="icon" class="rounded-full w-6 h-6" variant="outline">
                  <Cog class="w-full justify-center" :size="14"/>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent>
                <DropdownMenuGroup>
                  <DropdownMenuItem class="cursor-pointer">
                    <RouterLink :to="`/admin/query/history/${row?.code}`" target="_blank" class="flex items-center">
                      <Quote class="mr-2 h-4 w-4"/>
                      <span>{{ $t('query.common.quoteRecord') }}</span>
                    </RouterLink>
                  </DropdownMenuItem>
                  <DropdownMenuItem class="cursor-pointer" @click="handlerShowContent(true, row?.content)">
                    <SquareChevronRight class="mr-2 h-4 w-4"/>
                    <span>{{ $t('query.common.showSql') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem class="cursor-pointer" :disabled="row.state === 'FAILURE'" @click="handlerShowData(true, row)">
                    <Table class="mr-2 h-4 w-4"/>
                    <span>{{ $t('query.common.historyData') }}</span>
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </template>
      </TableCommon>
    </Card>
  </div>
  <SqlInfo v-if="contentVisible && content" :is-visible="contentVisible" :content="content" @close="handlerShowContent(false, null)"/>
  <HistoryData v-if="dataVisible && dataInfo" :is-visible="dataVisible" :info="dataInfo" @close="handlerShowData(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { Cog, Quote, SquareChevronRight, Table, TriangleAlert } from 'lucide-vue-next'
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
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import { HistoryModel } from '@/model/history'
import HistoryData from '@/views/pages/admin/history/HistoryData.vue'

export default defineComponent({
  name: 'HistoryHome',
  components: {
    HistoryData,
    SqlInfo,
    Button,
    TableCommon,
    TriangleAlert, Cog, SquareChevronRight, Quote, Table,
    Card,
    Tooltip,
    Avatar,
    Tag,
    DropdownMenuItem, DropdownMenuGroup, DropdownMenuSeparator, DropdownMenuLabel, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu
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
      dataInfo: null as HistoryModel | null,
      dataVisible: false,
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
      AuditService.getAll(this.filter)
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
    handlerShowContent(opened: boolean, value: string | null)
    {
      this.contentVisible = opened
      this.content = value
    },
    handlerShowData(opened: boolean, value: HistoryModel | null)
    {
      this.dataVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
