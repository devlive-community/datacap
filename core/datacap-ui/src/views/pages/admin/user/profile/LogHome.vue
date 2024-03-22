<template>
  <div class="w-full">
    <div>
      <h3 class="text-lg font-medium">{{ $t('user.common.log') }}</h3>
      <p class="text-sm text-muted-foreground">{{ $t('user.tip.log') }}</p>
    </div>
    <Separator class="my-4"/>
    <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
      <template #state="{row}">
        <Badge :style="{backgroundColor: Common.getColor(row?.state)}">{{ row?.state }}</Badge>
      </template>
    </TableCommon>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Separator } from '@/components/ui/separator'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHeaders } from './ProfileUtils'
import UserService from '@/services/user'
import { Badge } from '@/components/ui/badge'
import Common from '@/utils/common'

export default defineComponent({
  name: 'LogHome',
  computed: {
    Common()
    {
      return Common
    }
  },
  components: {
    Badge,
    TableCommon,
    Separator
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
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pagination: {} as PaginationModel
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      UserService.getLogs(this.filter)
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
});
</script>