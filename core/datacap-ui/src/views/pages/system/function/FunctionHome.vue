<template>
  <div class="w-full">
    <Card>
      <CardHeader class="border-b p-4">
        <CardTitle>{{ $t('function.common.list') }}</CardTitle>
      </CardHeader>
      <CardContent>
        <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
          <template #action="{row}">
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger as-child>
                  <Button variant="outline" size="sm" class="p-2" @click="handlerChangeInfo(true, row)">
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
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Pencil } from 'lucide-vue-next'
import { FilterModel } from '@/model/filter'
import { createHeaders } from '@/views/pages/system/function/FunctionUtils'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { RoleModel } from '@/model/role'
import FunctionService from '@/services/function'

export default defineComponent({
  name: 'FunctionHome',
  components: {
    Pencil,
    Card, CardHeader, CardTitle, CardContent,
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
      dataInfoVisible: false,
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
    }
  }
})
</script>
