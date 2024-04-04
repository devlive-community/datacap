<template>
  <div class="w-full">
    <Card>
      <CardHeader class="flex flex-row items-center border-b p-4">
        <div class="grid gap-2">
          <CardTitle>{{ $t('menu.common.list') }}</CardTitle>
        </div>
        <Button size="icon" class="ml-auto gap-1 h-6 w-6" @click="handlerChangeInfo(true, null)">
          <Plus :size="20"/>
        </Button>
      </CardHeader>
      <CardContent>
        <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
          <template #active="{row}">
            <Switch disabled :default-checked="row?.active"/>
          </template>
          <template #action="{row}">
            <div class="space-x-2">
              <TooltipProvider :delay-duration="0">
                <Tooltip>
                  <TooltipTrigger as-child>
                    <Button variant="outline" size="icon" class="p-2 w-8 h-8 rounded-full" @click="handlerChangeInfo(true, row)">
                      <Pencil :size="15"/>
                    </Button>
                  </TooltipTrigger>
                  <TooltipContent>
                    <p>{{ $t('common.editData') }}</p>
                  </TooltipContent>
                </Tooltip>
              </TooltipProvider>
            </div>
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
import { Pencil, Plus } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHeaders } from '@/views/pages/system/menu/MenuUtils'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { MenuModel } from '@/model/menu'
import MenuService from '@/services/menu'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { Switch } from '@/components/ui/switch'

export default defineComponent({
  name: 'MenuHome',
  components: {
    Switch,
    Button,
    Pencil, Plus,
    CardHeader, CardTitle, TableCommon, CardContent, Card,
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
      dataInfo: null as MenuModel | null
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
      MenuService.getAll(this.filter)
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
    handlerChangeInfo(isOpen: boolean, dataInfo: any)
    {
      this.dataInfoVisible = isOpen
      this.dataInfo = dataInfo
      if (!isOpen) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
