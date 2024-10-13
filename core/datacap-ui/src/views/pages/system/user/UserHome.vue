<template>
  <div class="w-full">
    <DataCapCard>
      <template #title>{{ $t('user.common.list') }}</template>
      <template #extra>
        <DcButton size="icon" class="ml-auto gap-1 h-6 w-6" @click="handlerChangeInfo(true, null)">
          <Plus :size="20"/>
        </DcButton>
      </template>
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
    </DataCapCard>
    <UserRole v-if="dataRoleVisible" :is-visible="dataRoleVisible" :info="dataInfo" @close="handlerChangeRole(false, null)"/>
  </div>
  <UserInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerChangeInfo(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import UserService from '@/services/user'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { useI18n } from 'vue-i18n'
import { createHeaders } from './UserUtils'
import { ArrowUpFromLine, Loader2, Plus } from 'lucide-vue-next'
import { DataCapCard } from '@/views/ui/card'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { Button } from '@/components/ui/button'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import UserRole from '@/views/pages/system/user/components/UserRole.vue'
import { UserModel } from '@/model/user'
import { Badge } from '@/components/ui/badge'
import DcButton from '@/views/ui/button/button.vue'
import UserInfo from '@/views/pages/system/user/UserInfo.vue'

export default defineComponent({
  name: 'UserHome',
  components: {
    DataCapCard,
    UserInfo,
    DcButton,
    Badge,
    UserRole,
    TooltipContent, TooltipTrigger, Tooltip, TooltipProvider,
    Button,
    TableCommon,
    Loader2, ArrowUpFromLine, Plus
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
      dataInfo: null as UserModel | null,
      dataInfoVisible: false
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
    },
    handlerChangeInfo(opened: boolean, dataInfo: UserModel | null)
    {
      this.dataInfoVisible = opened
      this.dataInfo = dataInfo
      if (!opened) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
