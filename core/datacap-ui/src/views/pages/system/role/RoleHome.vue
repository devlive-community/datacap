<template>
  <div class="w-full">
    <Card>
      <CardHeader class="border-b p-4">
        <CardTitle>{{ $t('role.common.list') }}</CardTitle>
      </CardHeader>
      <CardContent>
        <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
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
              <TooltipProvider :delay-duration="0">
                <Tooltip>
                  <TooltipTrigger as-child>
                    <Button variant="outline" size="icon" class="p-2 w-8 h-8 rounded-full" @click="handlerAssignMenu(true, row)">
                      <Menu :size="15"/>
                    </Button>
                  </TooltipTrigger>
                  <TooltipContent>
                    <p>{{ $t('role.common.assignMenu').replace('$NAME', row?.name) }}</p>
                  </TooltipContent>
                </Tooltip>
              </TooltipProvider>
            </div>
          </template>
        </TableCommon>
      </CardContent>
    </Card>
    <RoleInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerChangeInfo(false, null)"/>
    <RoleMenu v-if="dataAllocationVisible" :is-visible="dataAllocationVisible" :info="dataInfo" @close="handlerAssignMenu(false, null)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Button } from '@/components/ui/button'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { Menu, Pencil } from 'lucide-vue-next'
import { FilterModel } from '@/model/filter'
import { createHeaders } from '@/views/pages/system/role/RoleUtils'
import { useI18n } from 'vue-i18n'
import { PaginationModel } from '@/model/pagination'
import RoleService from '@/services/role'
import RoleInfo from '@/views/pages/system/role/RoleInfo.vue'
import { RoleModel } from '@/model/role'
import RoleMenu from '@/views/pages/system/role/RoleMenu.vue'

export default defineComponent({
  name: 'RoleHome',
  components: {
    RoleMenu,
    RoleInfo,
    TooltipContent, TooltipTrigger, TooltipProvider, Tooltip,
    Card, CardHeader, CardTitle, TableCommon, CardContent,
    Pencil, Menu,
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
      dataInfoVisible: false,
      dataAllocationVisible: false,
      data: [],
      pagination: {} as PaginationModel,
      dataInfo: null as RoleModel | null
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
      RoleService.getAll(this.filter)
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
      this.dataInfoVisible = isOpen
      this.dataInfo = dataInfo
      if (!isOpen) {
        this.handlerInitialize()
      }
    },
    handlerAssignMenu(opened: boolean, dataInfo: RoleModel | null)
    {
      this.dataAllocationVisible = opened
      this.dataInfo = dataInfo
    }
  }
})
</script>
