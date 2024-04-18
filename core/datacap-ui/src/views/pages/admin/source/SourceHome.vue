<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('source.common.list') }}</template>
      <template #extra>
        <Button size="icon" class="ml-auto gap-1 h-6 w-6" @click="handlerInfo(true, null)">
          <Plus :size="15"/>
        </Button>
      </template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #type="{row}">
          <Tooltip :content="row.type">
            <Avatar :size="'sm'" :src="'/static/images/plugin/' + row.type + '.png'" :alt="row.type" class="cursor-pointer"/>
          </Tooltip>
        </template>
        <template #public="{row}">
          <Switch disabled :default-checked="row.public"/>
        </template>
        <template #version="{row}">
          <Tag v-if="row.version">{{ row.version }}</Tag>
          <Tag v-else>-</Tag>
        </template>
        <template #available="{ row }">
          <Tooltip v-if="!row.available" :content="row.message">
            <CircleX class="cursor-pointer" style="color: #cc0000"/>
          </Tooltip>
          <CirclePlay v-else class="cursor-pointer" style="color: hsl(var(--primary))"/>
        </template>
        <template #action="{ row }">
          <div class="space-x-2">
            <Tooltip :content="$t('source.common.modify').replace('$NAME', row.name)">
              <Button :disabled="loginUserId !== row.user.id" size="icon" class="rounded-full w-6 h-6" @click="handlerInfo(true, row)">
                <Pencil :size="14"/>
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
                  <DropdownMenuItem :disabled="(loginUserId !== row.user.id) || !row.available" class="cursor-pointer">
                    <RouterLink :to="`/admin/source/${row?.code}`" target="_blank" class="flex items-center">
                      <Cog class="mr-2 h-4 w-4"/>
                      <span>{{ $t('source.common.manager') }}</span>
                    </RouterLink>
                  </DropdownMenuItem>
                  <DropdownMenuItem :disabled="loginUserId !== row.user.id" class="cursor-pointer" @click="handlerDelete(true, row)">
                    <Trash class="mr-2 h-4 w-4"/>
                    <span>{{ $t('common.deleteData') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem :disabled="(loginUserId !== row.user.id)" class="cursor-pointer" @click="handlerHistory(true, row)">
                    <History class="mr-2 h-4 w-4"/>
                    {{ $t('source.common.syncHistory') }}
                  </DropdownMenuItem>
                  <DropdownMenuItem :disabled="(loginUserId !== row.user.id) || !row.available" class="cursor-pointer" @click="handlerSyncMetadata(true, row)">
                    <RefreshCcwDot class="mr-2 h-4 w-4"/>
                    {{ $t('source.common.syncMetadata') }}
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </template>
      </TableCommon>
    </Card>
    <SourceInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
    <SourceDelete v-if="dataDeleteVisible" :is-visible="dataDeleteVisible" :info="dataInfo" @close="handlerDelete(false, null)"/>
    <SourceMetadata v-if="dataSyncMetadataVisible" :is-visible="dataSyncMetadataVisible" :info="dataInfo" @close="handlerSyncMetadata(false, null)"/>
    <SourceHistory v-if="dataHistoryVisible" :is-visible="dataHistoryVisible" :info="dataInfo" @close="handlerHistory(false, null)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import Button from '@/views/ui/button'
import { CirclePlay, CircleX, Cog, History, Info, Pencil, Plus, RefreshCcwDot, Trash } from 'lucide-vue-next'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'
import { createHeaders } from '@/views/pages/admin/source/SourceUtils'
import Avatar from '@/views/ui/avatar'
import Tooltip from '@/views/ui/tooltip'
import Tag from '@/views/ui/tag'
import { Switch } from '@/components/ui/switch'
import Common from '@/utils/common'
import SourceInfo from '@/views/pages/admin/source/SourceInfo.vue'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import SourceDelete from '@/views/pages/admin/source/SourceDelete.vue'
import SourceMetadata from '@/views/pages/admin/source/SourceMetadata.vue'
import SourceHistory from '@/views/pages/admin/source/SourceHistory.vue'

export default defineComponent({
  name: 'SourceHome',
  components: {
    Info,
    SourceHistory,
    SourceMetadata,
    SourceDelete,
    DropdownMenuItem, DropdownMenuGroup, DropdownMenuSeparator, DropdownMenuLabel, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu,
    SourceInfo,
    Tag,
    Tooltip,
    Switch,
    Avatar,
    TableCommon,
    Pencil, CircleX, CirclePlay, Cog, Trash, Plus, RefreshCcwDot, History,
    Button,
    Card
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(useI18n())
    const loginUserId = Common.getCurrentUserId()

    return {
      filter,
      headers,
      loginUserId
    }
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pagination: {} as PaginationModel,
      dataInfoVisible: false,
      dataInfo: null as SourceModel | null,
      dataDeleteVisible: false,
      dataSyncMetadataVisible: false,
      dataHistoryVisible: false
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
      SourceService.getAll(this.filter)
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
    handlerInfo(opened: boolean, value: null | SourceModel)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerDelete(opened: boolean, value: null | SourceModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerSyncMetadata(opened: boolean, value: null | SourceModel)
    {
      this.dataSyncMetadataVisible = opened
      this.dataInfo = value
    },
    handlerHistory(opened: boolean, value: null | SourceModel)
    {
      this.dataHistoryVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
