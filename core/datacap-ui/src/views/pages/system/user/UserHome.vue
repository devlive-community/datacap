<template>
  <div class="w-full">
    <Card>
      <CardHeader class="border-b p-4">
        <CardTitle>ddd</CardTitle>
      </CardHeader>
      <CardContent>
        <Loader2 v-if="loading" class="w-4 h-4 mr-2 animate-spin"/>
        <TableCommon v-else :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
          <template #action="{row}">
          </template>
        </TableCommon>
      </CardContent>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import UserService from '@/services/user'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { useI18n } from 'vue-i18n'
import { createHeaders } from './UserUtils'
import { Loader2 } from 'lucide-vue-next';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { PaginationModel } from '@/model/pagination'

export default defineComponent({
  name: 'UserHome',
  components: {CardContent, CardHeader, CardTitle, Card, Loader2, TableCommon},
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
      data: [],
      pagination: null as PaginationModel | null
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
              const pagination: PaginationModel = {
                pageSize: response.data.size,
                total: response.data.total,
                currentPage: response.data.page
              }
              this.pagination = pagination
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
