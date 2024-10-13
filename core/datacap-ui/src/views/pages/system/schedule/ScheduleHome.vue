<template>
  <div class="w-full">
    <DataCapCard>
      <template #title>{{ $t('schedule.common.list') }}</template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #active="{row}">
          <Switch disabled :default-checked="row.active"/>
        </template>
        <template #system="{row}">
          <Switch disabled :default-checked="row.system"/>
        </template>
        <template #action="{row}">
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger as-child>
                <Button variant="outline" size="sm" class="p-2" @click="handlerChangeInfo(true, row)">
                  <History :size="15"></History>
                </Button>
              </TooltipTrigger>
              <TooltipContent>
                <p>{{ $t('schedule.common.history') }}</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </template>
      </TableCommon>
    </DataCapCard>
    <ScheduleHistory v-if="dataHistoryVisible" :is-visible="dataHistoryVisible" :info="dataInfo" @close="handlerChangeInfo(false, null)"></ScheduleHistory>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { DataCapCard } from '@/views/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Button } from '@/components/ui/button'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { ArrowBigUp, History, Pencil } from 'lucide-vue-next'
import { FilterModel } from '@/model/filter'
import { createHeaders } from './ScheduleUtils'
import { useI18n } from 'vue-i18n'
import { PaginationModel } from '@/model/pagination'
import ScheduleService from '@/services/schedule'
import { Switch } from '@/components/ui/switch'
import ScheduleHistory from '@/views/pages/system/schedule/ScheduleHistory.vue'
import { ScheduleModel } from '@/model/schedule'

export default defineComponent({
  name: 'ScheduleHome',
  components: {
    DataCapCard,
    ScheduleHistory,
    ArrowBigUp, History, Pencil,
    Button, Switch,
    Tooltip, TooltipProvider, TooltipContent, TooltipTrigger,
    TableCommon
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
      dataHistoryVisible: false,
      data: [],
      pagination: {} as PaginationModel,
      dataInfo: null as ScheduleModel | null
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
      ScheduleService.getAll(this.filter)
                     .then((response) => {
                       if (response.status) {
                         this.data = response.data.content
                         this.pagination = {
                           pageSize: response.data.size,
                           total: response.data.total,
                           currentPage: response.data.page
                         }
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
    handlerChangeInfo(isOpen: boolean, dataInfo: any)
    {
      this.dataHistoryVisible = isOpen
      this.dataInfo = dataInfo
    }
  }
})
</script>