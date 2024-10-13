<template>
  <div class="w-full">
    <DataCapCard>
      <template #title>{{ $t('function.common.list') }}</template>
      <template #extra>
        <Button size="icon" class="ml-auto gap-1 h-6 w-6" @click="handlerInfo(true, null)">
          <Plus :size="20"/>
        </Button>
        <DropdownMenu>
          <DropdownMenuTrigger as-child>
            <Button variant="secondary" size="icon" class="gap-1 h-6 w-6 ml-2">
              <Cog class="w-full justify-center" :size="20"/>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuGroup>
              <DropdownMenuItem class="cursor-pointer" @click="handlerImport(true)">
                <Import class="mr-2 h-4 w-4"/>
                <span>{{ $t('function.common.import') }}</span>
              </DropdownMenuItem>
            </DropdownMenuGroup>
          </DropdownMenuContent>
        </DropdownMenu>
      </template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #plugin="{row}">
          <div class="flex items-center p-4 sm:justify-between">
            <div class="flex -space-x-2 overflow-hidden">
              <Avatar v-for="item in row?.plugin" size="sm" class="border-2 border-background w-8 h-8 cursor-pointer">
                <AvatarImage :src="'/static/images/plugin/' + item + '.png'"/>
                <AvatarFallback>{{ item }}</AvatarFallback>
              </Avatar>
            </div>
          </div>
        </template>
        <template #type="{ row }">
          <Badge>{{ $t('function.common.' + row.type.toLowerCase()) }}</Badge>
        </template>
        <template #action="{row}">
          <TooltipProvider :delay-duration="0">
            <Tooltip>
              <TooltipTrigger as-child>
                <Button variant="outline" size="icon" class="rounded-full" @click="handlerInfo(true, row)">
                  <Pencil :size="15"></Pencil>
                </Button>
              </TooltipTrigger>
              <TooltipContent>
                <p>{{ $t('common.editData') }}</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </template>
      </TableCommon>
    </DataCapCard>
    <FunctionInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
    <FunctionImport v-if="dataImportVisible" :is-visible="dataImportVisible" @close="handlerImport(false)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { DataCapCard } from '@/views/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Cog, Import, Pencil, Plus } from 'lucide-vue-next'
import { FilterModel } from '@/model/filter'
import { createHeaders } from '@/views/pages/system/function/FunctionUtils'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import FunctionService from '@/services/function'
import FunctionInfo from '@/views/pages/system/function/FunctionInfo.vue'
import { Button } from '@/components/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import FunctionImport from '@/views/pages/system/function/FunctionImport.vue'
import { FunctionModel } from '@/model/function'

export default defineComponent({
  name: 'FunctionHome',
  components: {
    FunctionImport,
    DataCapCard,
    Import, Pencil, Cog, Plus,
    Badge,
    Button,
    FunctionInfo,
    TableCommon,
    Avatar, AvatarFallback, AvatarImage,
    Tooltip, TooltipContent, TooltipProvider, TooltipTrigger,
    DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger
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
      dataInfoVisible: false,
      dataImportVisible: false,
      data: [],
      pagination: {} as PaginationModel,
      dataInfo: null as FunctionModel | null
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
      FunctionService.getAll(this.filter)
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
    handlerInfo(opened: boolean, value: FunctionModel | null)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerImport(value: boolean)
    {
      this.dataImportVisible = value
      this.handlerInitialize()
    }
  }
})
</script>
