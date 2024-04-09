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
        <template #action="{ row }">
          <div class="space-x-2">
            <Tooltip :content="$t('report.common.view').replace('$VALUE', row.name)">
              <Button size="icon" class="rounded-full w-6 h-6" @click="handlerView(true, row)">
                <Eye :size="14"/>
              </Button>
            </Tooltip>
          </div>
        </template>
      </TableCommon>
    </Card>
  </div>
  <ReportView v-if="dataViewVisible" :is-visible="dataViewVisible" :info="dataInfo" @close="handlerView(false, null)"/>
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
import Button from '@/views/ui/button'
import { Eye } from 'lucide-vue-next'
import { ReportModel } from '@/model/report'
import ReportView from '@/views/pages/admin/report/ReportView.vue'

export default defineComponent({
  name: 'ReportHome',
  components: {
    ReportView,
    TableCommon,
    Card,
    Switch,
    Tooltip,
    Avatar,
    Tag,
    Button,
    Eye
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
      dataInfo: null as ReportModel | null,
      dataViewVisible: false
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
    },
    handlerView(opened: boolean, value: ReportModel | null)
    {
      this.dataViewVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
