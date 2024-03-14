<template>
  <div class="w-full">
    <Card>
      <CardHeader class="border-b p-4">
        <CardTitle>{{ $t('schedule.common.list') }}</CardTitle>
      </CardHeader>
      <CardContent>
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
      </CardContent>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Button } from '@/components/ui/button'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { ArrowBigUp, History, Pencil } from 'lucide-vue-next'
import { FilterModel } from '@/model/filter'
import { createHeaders } from './ScheduleUtils'
import { useI18n } from 'vue-i18n'
import { PaginationModel } from '@/model/pagination'
import ScheduleService from '@/services/schedule'
import { Switch } from '@/components/ui/switch';

export default defineComponent({
  name: 'ScheduleHome',
  components: {
    ArrowBigUp, History, Pencil,
    Button, Switch,
    Card, CardHeader, CardTitle, CardContent,
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
    }
  }
})
</script>