<template>
  <div class="w-full">
    <Card>
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
            <!--            <DropdownMenu>-->
            <!--              <DropdownMenuTrigger as-child>-->
            <!--                <Button size="icon" class="rounded-full w-6 h-6" variant="outline">-->
            <!--                  <Cog class="w-full justify-center" :size="14"/>-->
            <!--                </Button>-->
            <!--              </DropdownMenuTrigger>-->
            <!--              <DropdownMenuContent>-->
            <!--                <DropdownMenuGroup>-->
            <!--                  <DropdownMenuItem :disabled="(loginUserId !== row.user.id) || !row.available" class="cursor-pointer">-->
            <!--                    <RouterLink :to="`/admin/source/${row?.id}/manager`" target="_blank" class="flex items-center">-->
            <!--                      <Cog class="mr-2 h-4 w-4"/>-->
            <!--                      <span>{{ $t('source.common.manager') }}</span>-->
            <!--                    </RouterLink>-->
            <!--                  </DropdownMenuItem>-->
            <!--                  <DropdownMenuItem :disabled="loginUserId !== row.user.id" class="cursor-pointer" @click="handlerDelete(true, row)">-->
            <!--                    <Trash class="mr-2 h-4 w-4"/>-->
            <!--                    <span>{{ $t('common.deleteData') }}</span>-->
            <!--                  </DropdownMenuItem>-->
            <!--                  <DropdownMenuItem :disabled="(loginUserId !== row.user.id)" class="cursor-pointer" @click="handlerHistory(true, row)">-->
            <!--                    <History class="mr-2 h-4 w-4"/>-->
            <!--                    {{ $t('source.common.syncHistory') }}-->
            <!--                  </DropdownMenuItem>-->
            <!--                  <DropdownMenuItem :disabled="(loginUserId !== row.user.id) || !row.available" class="cursor-pointer" @click="handlerSyncMetadata(true, row)">-->
            <!--                    <RefreshCcwDot class="mr-2 h-4 w-4"/>-->
            <!--                    {{ $t('source.common.syncMetadata') }}-->
            <!--                  </DropdownMenuItem>-->
            <!--                </DropdownMenuGroup>-->
            <!--              </DropdownMenuContent>-->
            <!--            </DropdownMenu>-->
          </div>
        </template>
      </TableCommon>
    </Card>
  </div>
  <SnippetInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
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
import { Pencil } from 'lucide-vue-next'
import { SnippetModel } from '@/model/snippet'
import SnippetInfo from '@/views/pages/admin/snippet/SnippetInfo.vue'

export default defineComponent({
  name: 'SnippetHome',
  components: {
    SnippetInfo,
    Pencil,
    TableCommon,
    Card,
    Avatar,
    Tooltip,
    Button
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
      dataInfo: null as null | SnippetModel
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
    }
  }
})
</script>
