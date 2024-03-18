<template>
  <div class="w-full">
    <Card>
      <CardHeader class="border-b p-4">
        <CardTitle>{{ $t('dataset.common.list') }}</CardTitle>
      </CardHeader>
      <CardContent>
        <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
          <template #source="{row}">
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger as-child>
                  <Avatar size="sm">
                    <AvatarImage :src="'/static/images/plugin/' + row?.source.type + '.png'" :alt="row?.source.type"/>
                    <AvatarFallback>{{ row?.source.type }}</AvatarFallback>
                  </Avatar>
                </TooltipTrigger>
                <TooltipContent align="center">{{ row?.source.type }}</TooltipContent>
              </Tooltip>
            </TooltipProvider>
          </template>
          <template #syncMode="{ row }">
            <Badge v-if="row?.syncMode === 'MANUAL'">{{ $t('dataset.common.syncModeManual') }}</Badge>
            <Badge v-else-if="row?.syncMode === 'TIMING'">{{ $t('dataset.common.syncModeTiming') }}</Badge>
            <Badge v-else-if="row?.syncMode === 'OUT_SYNC'">{{ $t('dataset.common.syncModeOutSync') }}</Badge>
          </template>
          <template #state="{ row }">
            <HoverCard>
              <HoverCardTrigger>{{ getState(row?.state) }}</HoverCardTrigger>
              <HoverCardContent>
                <DatasetState class="mt-[25px]" :states="row?.state"/>
              </HoverCardContent>
            </HoverCard>
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
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { createHeaders } from './DatasetUtils'
import DatasetService from '@/services/dataset'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import DatasetState from '@/views/pages/admin/dataset/components/DatasetState.vue'
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@/components/ui/hover-card';

export default defineComponent({
  name: 'DatasetHome',
  components: {
    HoverCardContent, HoverCardTrigger, HoverCard,
    DatasetState,
    Badge,
    AvatarFallback, AvatarImage, Avatar,
    TooltipTrigger, TooltipProvider, TooltipContent, Tooltip,
    TableCommon,
    CardContent, CardHeader, CardTitle, Card
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
      DatasetService.getAll(this.filter)
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
    getState(state: Array<any> | null): string | null
    {
      if (state && state.length > 0) {
        return state[state.length - 1]
      }
      return null
    }
  }
})

</script>
