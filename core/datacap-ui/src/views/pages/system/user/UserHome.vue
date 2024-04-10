<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('user.common.list') }}</template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #role="{row}">
          <Badge v-for="role in row.roles" class="mt-1">
            {{ role.name }}
          </Badge>
        </template>
        <template #action="{row}">
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger as-child>
                <Button variant="outline" size="sm" class="p-3" @click="handlerChangeRole(true, row)">
                  <ArrowUpFromLine :size="15"></ArrowUpFromLine>
                </Button>
              </TooltipTrigger>
              <TooltipContent>
                <p>{{ $t('user.common.assignRole') }}</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </template>
      </TableCommon>
    </Card>
    <UserRole v-if="dataRoleVisible" :is-visible="dataRoleVisible" :info="dataInfo" @close="handlerChangeRole(false, null)"></UserRole>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import UserService from '@/services/user'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { useI18n } from 'vue-i18n'
import { createHeaders } from './UserUtils'
import { ArrowUpFromLine, Loader2 } from 'lucide-vue-next'
import Card from '@/views/ui/card'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { Button } from '@/components/ui/button'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import UserRole from '@/views/pages/system/user/components/UserRole.vue'
import { UserModel } from '@/model/user'
import { Badge } from '@/components/ui/badge'

export default defineComponent({
  name: 'UserHome',
  components: {
    Badge,
    UserRole,
    TooltipContent, TooltipTrigger, Tooltip, TooltipProvider,
    Card,
    Button,
    TableCommon,
    Loader2, ArrowUpFromLine
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const i18n = useI18n()
    const headers = createHeaders(i18n)

    return {
      filter,
      headers
    }
  },
  data()
  {
    return {
      loading: false,
      dataRoleVisible: false,
      data: [],
      pagination: {} as PaginationModel,
      dataInfo: null as UserModel | null
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
      UserService.getAll(this.filter)
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
    handlerChangeRole(isOpen: boolean, dataInfo: UserModel | null)
    {
      this.dataRoleVisible = isOpen
      this.dataInfo = dataInfo
      if (!isOpen) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
