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
          <template #action="{row}">
            <DropdownMenu>
              <DropdownMenuTrigger as-child>
                <Button variant="outline">
                  <Cog class="w-full justify-center" :size="20"/>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent>
                <DropdownMenuGroup>
                  <DropdownMenuItem :disabled="!isSuccess(row?.state)">
                    <RouterLink :to="`/admin/dataset/adhoc/${row?.code}`" target="_blank" class="flex items-center">
                      <BarChart2 class="mr-2 h-4 w-4"/>
                      <span>{{ $t('dataset.common.adhoc') }}</span>
                    </RouterLink>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator/>
                  <DropdownMenuItem :disabled="!isSuccess(row?.state)" style="cursor: pointer;" @click="handlerSyncData(row, true)">
                    <RefreshCcw class="mr-2 h-4 w-4"/>
                    <span>{{ $t('dataset.common.syncData') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem style="cursor: pointer;" @click="handlerHistory(row, true)">
                    <History class="mr-2 h-4 w-4"/>
                    <span>{{ $t('dataset.common.history') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator/>
                  <DropdownMenuItem :disabled="isSuccess(row?.state)" style="cursor: pointer;" @click="handlerError(row, true)">
                    <TriangleAlert class="mr-2 h-4 w-4"/>
                    <span>{{ $t('dataset.common.error') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem :disabled="isSuccess(row?.state)" style="cursor: pointer;" @click="handlerRebuild(row, true)">
                    <CirclePlay v-if="row?.state === 'SUCCESS'" class="mr-2 h-4 w-4"/>
                    <CircleStop v-else class="mr-2 h-4 w-4"/>
                    {{ $t('dataset.common.rebuild') }}
                  </DropdownMenuItem>
                  <DropdownMenuItem :disabled="!(row?.totalRows > 0)" style="cursor: pointer;" @click="handlerClearData(row, true)">
                    <SquareX class="mr-2 h-4 w-4"/>
                    {{ $t('dataset.common.clearData') }}
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          </template>
        </TableCommon>
      </CardContent>
    </Card>
    <DatasetRebuild v-if="rebuildVisible" :is-visible="rebuildVisible" :data="contextData" @close="handlerRebuild(null, false)"/>
    <DatasetHistory v-if="historyVisible" :is-visible="historyVisible" :info="contextData" @close="handlerHistory(null, false)"/>
    <DatasetSync v-if="syncDataVisible" :is-visible="syncDataVisible" :info="contextData" @close="handlerSyncData(null, false)"/>
    <DatasetClear v-if="clearDataVisible" :is-visible="clearDataVisible" :info="contextData" @close="handlerClearData(null, false)"/>
    <MarkdownPreview v-if="errorVisible && contextData" :is-visible="errorVisible" :content="'```java\n' + contextData.message + '\n```'" @close="handlerError(null, false)"/>
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
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@/components/ui/hover-card'
import { BarChart2, CirclePlay, CircleStop, Cog, History, RefreshCcw, SquareX, TriangleAlert } from 'lucide-vue-next'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import { DatasetModel } from '@/model/dataset'
import DatasetRebuild from '@/views/pages/admin/dataset/DatasetRebuild.vue'
import DatasetHistory from '@/views/pages/admin/dataset/DatasetHistory.vue'
import DatasetSync from '@/views/pages/admin/dataset/DatasetSync.vue'
import DatasetClear from '@/views/pages/admin/dataset/DatasetClear.vue'
import MarkdownPreview from '@/views/components/markdown/MarkdownView.vue';

export default defineComponent({
  name: 'DatasetHome',
  components: {
    MarkdownPreview,
    DatasetClear,
    DatasetSync,
    DatasetHistory,
    DatasetRebuild,
    DropdownMenuItem, DropdownMenuGroup, DropdownMenuSeparator, DropdownMenuLabel, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu,
    HoverCardContent, HoverCardTrigger, HoverCard,
    DatasetState,
    Badge,
    AvatarFallback, AvatarImage, Avatar,
    TooltipTrigger, TooltipProvider, TooltipContent, Tooltip,
    TableCommon,
    CardContent, CardHeader, CardTitle, Card,
    Cog, BarChart2, CirclePlay, CircleStop, History, RefreshCcw, SquareX, TriangleAlert
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
      contextData: null as DatasetModel | null,
      rebuildVisible: false,
      historyVisible: false,
      syncDataVisible: false,
      clearDataVisible: false,
      errorVisible: false
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
    handlerRebuild(record: DatasetModel | null, opened: boolean)
    {
      if (record && this.isSuccess(record.state)) {
        return
      }
      this.rebuildVisible = opened
      this.contextData = record
    },
    handlerHistory(record: DatasetModel | null, opened: boolean)
    {
      this.contextData = record
      this.historyVisible = opened
    },
    handlerSyncData(record: DatasetModel | null, opened: boolean)
    {
      if (record && !this.isSuccess(record.state)) {
        return
      }
      this.contextData = record
      this.syncDataVisible = opened
    },
    handlerClearData(record: DatasetModel | null, opened: boolean)
    {
      if (record && !(record.totalRows > 0)) {
        return
      }
      this.contextData = record
      this.clearDataVisible = opened
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerError(record: DatasetModel | null, opened: boolean)
    {
      this.errorVisible = opened
      this.contextData = record
    },
    getState(state: Array<any> | null): string | null
    {
      if (state && state.length > 0) {
        return state[state.length - 1]
      }
      return null
    },
    isSuccess(state: Array<any> | null)
    {
      if (state && state.length > 0) {
        return state[state.length - 1].endsWith('SUCCESS')
      }
      return false
    }
  }
})

</script>
