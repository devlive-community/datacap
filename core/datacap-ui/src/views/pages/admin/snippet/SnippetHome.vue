<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('snippet.common.list') }}</template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #username="{ row }">
          <Avatar :src="row.user.avatar" :alt="row.user.username"/>
        </template>
        <template #action="{ row }">
          <div class="space-x-2">
            <Tooltip :content="$t('snippet.common.modify').replace('$VALUE', row.name)">
              <Button size="icon" class="rounded-full w-6 h-6" @click="handlerInfo(true, row)">
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
                  <DropdownMenuItem class="cursor-pointer">
                    <RouterLink :to="`/admin/query/snippet/${row?.code}`" target="_blank" class="flex items-center">
                      <Quote class="mr-2 h-4 w-4"/>
                      <span>{{ $t('query.common.quoteRecord') }}</span>
                    </RouterLink>
                  </DropdownMenuItem>
                  <DropdownMenuItem class="cursor-pointer" @click="handlerShowContent(true, row?.context)">
                    <SquareChevronRight class="mr-2 h-4 w-4"/>
                    <span>{{ $t('query.common.showSql') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem class="cursor-pointer" @click="handlerDelete(true, row)">
                    <Delete class="mr-2 h-4 w-4"/>
                    <span>{{ $t('snippet.common.delete') }}</span>
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </template>
      </TableCommon>
    </Card>
  </div>
  <SnippetInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
  <SnippetDelete v-if="dataDeleteVisible" :is-visible="dataDeleteVisible" :info="dataInfo" @close="handlerDelete(false, null)"/>
  <SqlInfo v-if="contentVisible && content" :is-visible="contentVisible" :content="content" @close="handlerShowContent(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { FilterModel } from '@/model/filter.ts'
import { createHeaders } from '@/views/pages/admin/snippet/SnippetUtils'
import { useI18n } from 'vue-i18n'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import SnippetService from '@/services/snippet'
import { ToastUtils } from '@/utils/toast'
import Avatar from '@/views/ui/avatar'
import Tooltip from '@/views/ui/tooltip'
import Button from '@/views/ui/button'
import { Cog, Delete, Pencil, Quote, SquareChevronRight } from 'lucide-vue-next'
import { SnippetModel } from '@/model/snippet'
import SnippetInfo from '@/views/pages/admin/snippet/SnippetInfo.vue'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import SnippetDelete from '@/views/pages/admin/snippet/SnippetDelete.vue'

export default defineComponent({
  name: 'SnippetHome',
  components: {
    SnippetDelete,
    SqlInfo,
    SnippetInfo,
    Quote, Pencil, Cog, SquareChevronRight, Delete,
    TableCommon,
    Card,
    Avatar,
    Tooltip,
    Button,
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
      dataInfoVisible: false,
      dataInfo: null as null | SnippetModel,
      contentVisible: false,
      content: null as string | null,
      dataDeleteVisible: false
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
      SnippetService.getAll(this.filter)
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
    handlerInfo(opened: boolean, value: null | SnippetModel)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerShowContent(opened: boolean, value: string | null)
    {
      this.contentVisible = opened
      this.content = value
    },
    handlerDelete(opened: boolean, data: null | SnippetModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = data
      if (!opened) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
