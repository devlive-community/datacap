<template>
  <div class="w-full">
    <Card>
      <CardHeader class="flex flex-row items-center border-b p-4">
        <div class="grid gap-2">
          <CardTitle>{{ $t('function.common.list') }}</CardTitle>
        </div>
        <Button size="icon" class="ml-auto gap-1 h-6 w-6" @click="handlerInfo(true)">
          <Plus :size="20"/>
        </Button>
      </CardHeader>
      <CardContent>
        <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
          <template #plugin="{row}">
            <Avatar>
              <AvatarImage :src="'/static/images/plugin/' + row?.plugin + '.png'"/>
              <AvatarFallback>{{ row?.plugin }}</AvatarFallback>
            </Avatar>
          </template>
          <template #type="{ row }">
            <Badge>{{ $t('function.common.' + row.type.toLowerCase()) }}</Badge>
          </template>
          <template #action="{row}">
            <TooltipProvider :delay-duration="0">
              <Tooltip>
                <TooltipTrigger as-child>
                  <Button variant="outline" size="icon" @click="handlerInfo(true, row?.id)">
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
      </CardContent>
    </Card>
    <FunctionInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :id="applyId" @close="handlerInfo(false)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Pencil, Plus } from 'lucide-vue-next'
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

export default defineComponent({
  name: 'FunctionHome',
  components: {
    Badge,
    Button,
    FunctionInfo,
    Pencil, Plus,
    Card, CardHeader, CardTitle, CardContent,
    TableCommon,
    Avatar, AvatarFallback, AvatarImage,
    Tooltip, TooltipContent, TooltipProvider, TooltipTrigger
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
      data: [],
      pagination: {} as PaginationModel,
      applyId: 0
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
    handlerInfo(opened: boolean, value?: number)
    {
      this.dataInfoVisible = opened
      if (value) {
        this.applyId = value
      }
      if (!opened) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
